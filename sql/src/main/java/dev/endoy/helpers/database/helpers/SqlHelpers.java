package dev.endoy.helpers.database.helpers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelpers
{

    private SqlHelpers()
    {
    }

    public static boolean doesTableExist( DataSource dataSource, String tableName ) throws SQLException
    {
        try ( Connection connection = dataSource.getConnection() )
        {
            return doesTableExist( connection, tableName );
        }
    }

    public static boolean doesTableExist( Connection connection, String tableName ) throws SQLException
    {
        DatabaseMetaData metaData = connection.getMetaData();

        try ( ResultSet resultSet = metaData.getTables( null, null, tableName, new String[]{ "TABLE" } ) )
        {
            return resultSet.next();
        }
    }
}
