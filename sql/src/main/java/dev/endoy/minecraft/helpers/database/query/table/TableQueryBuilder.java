package dev.endoy.minecraft.helpers.database.query.table;

import dev.endoy.minecraft.helpers.database.definitions.ColumnDefinition;
import dev.endoy.minecraft.helpers.database.SQLDialect;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TableQueryBuilder
{

    private final SQLDialect dialect;
    private String tableName;
    private boolean ifNotExists;
    private List<ColumnDefinition> columns = new ArrayList<>();

    public TableQueryBuilder name( String tableName )
    {
        this.tableName = tableName;
        return this;
    }

    public ColumnQueryBuilder addColumn()
    {
        return new ColumnQueryBuilder( this );
    }

    public TableQueryBuilder addColumn( ColumnDefinition columnDefinition )
    {
        columns.add( columnDefinition );
        return this;
    }

    public TableQueryBuilder ifNotExists()
    {
        this.ifNotExists = true;
        return this;
    }

    public TableQueryBuilder setColumns( List<ColumnDefinition> columns )
    {
        this.columns = columns;
        return this;
    }

    public String build()
    {
        return dialect.createTable( tableName, ifNotExists, columns );
    }
}
