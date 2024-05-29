package dev.endoy.minecraft.helpers.database.migration;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlMigratorTest
{

    private static MySQLContainer mySQLContainer;
    private static MysqlDataSource mysqlDataSource;

    @BeforeAll
    static void setUp()
    {
        mySQLContainer = new MySQLContainer( DockerImageName.parse( "mysql" ).withTag( "8.4.0" ) );
        mySQLContainer.start();
        mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL( mySQLContainer.getJdbcUrl() );
        mysqlDataSource.setUser( mySQLContainer.getUsername() );
        mysqlDataSource.setPassword( mySQLContainer.getPassword() );
    }

    @AfterEach
    void tearDown() throws SQLException
    {
        try ( Connection connection = mysqlDataSource.getConnection() )
        {
            try ( Statement statement = connection.createStatement() )
            {
                statement.executeUpdate( "DROP TABLE IF EXISTS migrations" );
                statement.executeUpdate( "DROP TABLE IF EXISTS test" );
            }
        }
    }

    @Test
    void testCreateMigrationSchema() throws SQLException
    {
        SqlMigrator.builder()
            .dataSource( mysqlDataSource )
            .initMigrationClass( V1__TestMigration.class )
            .build()
            .load();

        assertTrue( this.listTables().contains( "migrations" ) );
    }

    @Test
    void testMigrate() throws SQLException
    {
        SqlMigrator.builder()
            .dataSource( mysqlDataSource )
            .initMigrationClass( V1__TestMigration.class )
            .build()
            .load()
            .migrate();

        assertTrue( this.listTables().contains( "test" ) );

        try ( Connection connection = mysqlDataSource.getConnection();
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery( "SELECT * FROM test" ) )
        {
            List<String> names = new ArrayList<>();
            while ( resultSet.next() )
            {
                names.add( resultSet.getString( "name" ) );
            }

            assertTrue( names.contains( "hello" ) );
            assertTrue( names.contains( "world" ) );
        }
    }

    private List<String> listTables() throws SQLException
    {
        List<String> tables = new ArrayList<>();

        try ( Connection connection = mysqlDataSource.getConnection();
              Statement statement = connection.createStatement();
              ResultSet resultSet = statement.executeQuery( "SELECT table_name FROM information_schema.tables;" ) )
        {
            while ( resultSet.next() )
            {
                tables.add( resultSet.getString( "table_name" ) );
            }
        }

        return tables;
    }

    public static class V1__TestMigration implements Migration
    {

        @Override
        public void migrate( Connection connection ) throws SQLException
        {

            try ( Statement statement = connection.createStatement() )
            {
                statement.executeUpdate( """
                    CREATE TABLE test (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
                    );
                    """ );

                statement.executeUpdate( "INSERT INTO test (name) VALUES ('hello'), ('world')" );
            }
        }
    }
}