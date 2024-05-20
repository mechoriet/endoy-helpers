package dev.endoy.minecraft.helpers.database.migration;

import java.sql.Connection;

public interface Migration
{

    void migrate( Connection connection );

}
