package dev.endoy.helpers.database.migration;

import dev.endoy.helpers.database.helpers.SqlHelpers;
import dev.endoy.helpers.common.logger.EndoyLogger;
import dev.endoy.helpers.common.utils.ReflectionUtils;
import lombok.Builder;
import lombok.Value;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Builder
public class SqlMigrator
{

    private static final Logger ENDOY_LOGGER = EndoyLogger.forClass( SqlMigrator.class );
    private final DataSource dataSource;
    private final Class<?> initMigrationClass;

    public SqlMigrator load()
    {
        if ( dataSource == null )
        {
            throw new MigrationException( "DataSource is not set" );
        }
        if ( initMigrationClass == null )
        {
            throw new MigrationException( "InitMigrationClass is not set" );
        }

        try ( Connection connection = dataSource.getConnection() )
        {
            connection.setAutoCommit( false );

            if ( !SqlHelpers.doesTableExist( connection, "migrations" ) )
            {
                try ( Statement statement = connection.createStatement() )
                {
                    statement.executeUpdate( """
                        CREATE TABLE migrations (
                            id SERIAL PRIMARY KEY,
                            version INT NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            success BOOLEAN,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        );
                        """ );

                    connection.commit();
                }
                catch ( SQLException e )
                {
                    connection.rollback();
                    throw new MigrationException( e );
                }
            }
        }
        catch ( SQLException e )
        {
            throw new MigrationException( e );
        }
        return this;
    }

    public void migrate()
    {
        LinkedList<Migration> migrations = ReflectionUtils.getClassesInPackageImplementing( this.initMigrationClass, Migration.class )
            .stream()
            .map( ReflectionUtils::createInstance )
            .map( Migration.class::cast )
            .filter( Objects::nonNull )
            .sorted( Comparator.comparingInt( this::getMigrationVersion ) )
            .collect( Collectors.toCollection( LinkedList::new ) );

        for ( Migration migration : migrations )
        {
            int migrationVersion = this.getMigrationVersion( migration );

            try ( Connection connection = dataSource.getConnection() )
            {
                connection.setAutoCommit( false );

                Optional<MigrationRecord> optionalMigrationRecord = this.fetchMigrationRecordForVersion( connection, migrationVersion );

                if ( optionalMigrationRecord.isPresent() )
                {
                    MigrationRecord migrationRecord = optionalMigrationRecord.get();

                    if ( migrationRecord.isSuccess() )
                    {
                        if ( migrationRecord.getName().equalsIgnoreCase( migration.getClass().getSimpleName() ) )
                        {
                            ENDOY_LOGGER.info( "Skipping: " + migration.getClass().getSimpleName() + " since it has already been migrated." );
                        }
                        else
                        {
                            throw new MigrationException( "Another migration has already been run for version " + migrationVersion + ". (local: " + migration.getClass().getSimpleName() + ", db: " + migrationRecord.getName() + ")" );
                        }
                    }
                    else
                    {
                        throw new MigrationException( "Migration " + migrationRecord.getName() + " has failed during a previous run. Please delete the record from the migrations table to retry." );
                    }
                }
                else
                {
                    try
                    {
                        ENDOY_LOGGER.info( "Running migration: " + migration.getClass().getSimpleName() + "." );

                        migration.migrate( connection );

                        try ( PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO migrations (version, name, success) VALUES (?, ?, ?)" ) )
                        {
                            preparedStatement.setInt( 1, migrationVersion );
                            preparedStatement.setString( 2, migration.getClass().getSimpleName() );
                            preparedStatement.setBoolean( 3, true );
                            preparedStatement.executeUpdate();
                        }

                        connection.commit();
                    }
                    catch ( Exception e )
                    {
                        connection.rollback();
                        this.registerFailedMigration( migrationVersion, migration );
                        throw new MigrationException( e );
                    }
                }
            }
            catch ( SQLException e )
            {
                throw new MigrationException( e );
            }
        }
    }

    private int getMigrationVersion( Migration migration )
    {
        String className = migration.getClass().getSimpleName();
        return Integer.parseInt( className.substring( 1, className.indexOf( "__" ) ) );
    }

    private Optional<MigrationRecord> fetchMigrationRecordForVersion( Connection connection, int version ) throws SQLException
    {
        try ( PreparedStatement preparedStatement = connection.prepareStatement( "SELECT * FROM migrations WHERE version = ?" ) )
        {
            preparedStatement.setInt( 1, version );

            try ( ResultSet resultSet = preparedStatement.executeQuery() )
            {
                if ( resultSet.next() )
                {
                    return Optional.of( new MigrationRecord(
                        resultSet.getInt( "id" ),
                        resultSet.getInt( "version" ),
                        resultSet.getString( "name" ),
                        resultSet.getBoolean( "success" ),
                        resultSet.getTimestamp( "created_at" )
                    ) );
                }
            }
        }
        return Optional.empty();
    }

    private void registerFailedMigration( int version, Migration migration )
    {
        try ( Connection connection = dataSource.getConnection() )
        {
            connection.setAutoCommit( false );

            try ( PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO migrations (version, name, success) VALUES (?, ?, ?)" ) )
            {
                preparedStatement.setInt( 1, version );
                preparedStatement.setString( 2, migration.getClass().getSimpleName() );
                preparedStatement.setBoolean( 3, false );
                preparedStatement.executeUpdate();
            }

            connection.commit();
        }
        catch ( SQLException e )
        {
            throw new MigrationException( e );
        }
    }

    @Value
    static class MigrationRecord
    {
        int id;
        int version;
        String name;
        boolean success;
        Timestamp createdAt;
    }
}
