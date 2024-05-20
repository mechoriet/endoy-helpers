package dev.endoy.minecraft.helpers.database.migration;

import dev.endoy.minecraft.helpers.database.DialectType;
import dev.endoy.minecraft.helpers.database.definitions.ColumnDataType;
import dev.endoy.minecraft.helpers.database.definitions.DefaultValues;
import dev.endoy.minecraft.helpers.database.query.QueryBuilder;
import dev.endoy.minecraft.helpers.utils.ReflectionUtils;
import lombok.Builder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Builder
public class SqlMigrator
{

    private final DataSource dataSource;
    private final Class<?> initMigrationClass;

    public SqlMigrator load()
    {
        try ( Connection connection = dataSource.getConnection() )
        {
            connection.setAutoCommit( false );

            try ( Statement statement = connection.createStatement() )
            {
                statement.execute(
                    QueryBuilder.forDialect( DialectType.MYSQL )
                        .createTable()
                        .ifNotExists()
                        .name( "migrations" )
                        .addColumn().name( "id" ).type( ColumnDataType.SERIAL ).addConstraint().primaryKey()
                        .and()
                        .addColumn().name( "version" ).type( ColumnDataType.INT ).addConstraint().notNull()
                        .and()
                        .addColumn().name( "name" ).type( ColumnDataType.TEXT ).addConstraint().notNull()
                        .and()
                        .addColumn().name( "created_at" ).type( ColumnDataType.DATETIME ).addConstraint().notNull().defaultValue( DefaultValues.currentTimestamp() )
                        .and()
                        .addColumn().name( "success" ).type( ColumnDataType.BOOLEAN ).addConstraint().notNull().defaultValue( DefaultValues.literal( true ) )
                        .and()
                        .build()
                );
            }

            connection.commit();
        }
        catch ( SQLException e )
        {
            throw new MigrationException( e );
        }
        return this;
    }

    public void migrate()
    {
        ReflectionUtils.getClassesInPackageImplementing( this.initMigrationClass, Migration.class )
            .stream()
            .map( ReflectionUtils::createInstance )
            .map( Migration.class::cast )
            .filter( Objects::nonNull )
            .forEach( migration ->
            {
                try ( Connection connection = dataSource.getConnection() )
                {
                    connection.setAutoCommit( false );
                    migration.migrate( connection );
                    connection.commit();
                }
                catch ( SQLException e )
                {
                    throw new MigrationException( e );
                }
            } );
    }
}
