package dev.endoy.minecraft.helpers.database;

import dev.endoy.minecraft.helpers.database.dialect.MySQLDialect;
import lombok.Getter;

@Getter
public enum DialectType
{

    MYSQL( new MySQLDialect() ),
    POSTGRESQL( null ); // TODO: implement PostgreSQL dialect

    private final SQLDialect dialect;

    DialectType( SQLDialect dialect )
    {
        this.dialect = dialect;
    }
}
