package dev.endoy.minecraft.helpers.database.migration;

import lombok.Builder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Builder
public class MigrationManager
{

    private final DataSource dataSource;
    private final String migrationPath;

    public void migrate()
    {
        try ( Connection connection = dataSource.getConnection() )
        {
            connection.setAutoCommit( false );

            try ( PreparedStatement preparedStatement = connection.prepareStatement( """
                CREATE TABLE IF NOT EXISTS migrations (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """ ) )
            {
                preparedStatement.execute();
            }

            connection.commit();
        }
        catch ( SQLException e )
        {
            throw new MigrationException( e );
        }
    }
}
