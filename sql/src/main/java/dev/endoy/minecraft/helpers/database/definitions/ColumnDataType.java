package dev.endoy.minecraft.helpers.database.definitions;

import lombok.Getter;

@Getter
public enum ColumnDataType
{

    TINYINT( true ),
    SMALLINT( true ),
    INT( true ),
    BIGINT( true ),
    BIT( true ),
    DOUBLE( true, true ),
    FLOAT( true, true ),
    DECIMAL( true, true ),
    BOOLEAN,
    DATETIME,
    TIMESTAMP,
    DATE,
    TIME,
    TEXT,
    VARCHAR( true ),
    SERIAL;

    private final boolean supportsSize;
    private final boolean supportsDecimalSize;

    ColumnDataType()
    {
        this( false, false );
    }

    ColumnDataType( boolean supportsSize )
    {
        this( supportsSize, false );
    }

    ColumnDataType( boolean supportsSize, boolean supportsDecimalSize )
    {
        this.supportsSize = supportsSize;
        this.supportsDecimalSize = supportsDecimalSize;
    }
}
