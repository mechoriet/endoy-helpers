package dev.endoy.minecraft.helpers.database.migration;

import dev.endoy.minecraft.helpers.database.helpers.SqlHelpers;
import dev.endoy.minecraft.helpers.utils.ReflectionUtils;
import lombok.Builder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
public class SqlMigrator
{

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
                            name VARCHAR(255) NOT NULL,
                            success BOOLEAN,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        );
                        """ );
                }

                connection.commit();
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
            try ( Connection connection = dataSource.getConnection() )
            {
                if ( !this.shouldMigrate( connection, migration.getClass().getSimpleName() ) )
                {
                    break;
                }

                connection.setAutoCommit( false );
                migration.migrate( connection );

                try ( PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO migrations (name, success) VALUES (?, ?)" ) )
                {
                    preparedStatement.setString( 1, migration.getClass().getSimpleName() );
                    preparedStatement.setBoolean( 2, true );
                    preparedStatement.executeUpdate();
                }

                connection.commit();
            }
            catch ( SQLException e )
            {
                this.registerFailedMigration( migration );
                throw new MigrationException( e );
            }
        }
    }

    private int getMigrationVersion( Migration migration )
    {
        String className = migration.getClass().getSimpleName();
        return Integer.parseInt( className.substring( 1, className.indexOf( "__" ) ) );
    }

    private boolean shouldMigrate( Connection connection, String migrationName ) throws SQLException
    {
        try ( PreparedStatement preparedStatement = connection.prepareStatement( "SELECT COUNT(*) FROM migrations WHERE name = ? AND success = ?" ) )
        {
            preparedStatement.setString( 1, migrationName );
            preparedStatement.setBoolean( 2, true );

            try ( ResultSet resultSet = preparedStatement.executeQuery() )
            {
                return resultSet.next() && resultSet.getInt( 1 ) == 0;
            }
        }
    }

    private void registerFailedMigration( Migration migration )
    {
        try ( Connection connection = dataSource.getConnection() )
        {
            connection.setAutoCommit( false );

            try ( PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO migrations (name, success) VALUES (?, ?)" ) )
            {
                preparedStatement.setString( 1, migration.getClass().getSimpleName() );
                preparedStatement.setBoolean( 2, false );
                preparedStatement.executeUpdate();
            }

            connection.commit();
        }
        catch ( SQLException e )
        {
            throw new MigrationException( e );
        }
    }
}
