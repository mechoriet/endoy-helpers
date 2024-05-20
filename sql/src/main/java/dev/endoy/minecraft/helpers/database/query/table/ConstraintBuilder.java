package dev.endoy.minecraft.helpers.database.query.table;

import dev.endoy.minecraft.helpers.database.definitions.NotNullConstraintDefinition;
import dev.endoy.minecraft.helpers.database.definitions.ForeignKeyConstraintDefinition;
import dev.endoy.minecraft.helpers.database.definitions.PrimaryKeyConstraintDefinition;
import dev.endoy.minecraft.helpers.database.definitions.UniqueKeyConstraintDefinition;

public class ConstraintBuilder
{

    private final ColumnQueryBuilder columnQueryBuilder;

    public ConstraintBuilder( ColumnQueryBuilder columnQueryBuilder )
    {
        this.columnQueryBuilder = columnQueryBuilder;
    }

    public ColumnQueryBuilder unique()
    {
        return columnQueryBuilder.addConstraint( new UniqueKeyConstraintDefinition() );
    }

    public ColumnQueryBuilder notNull()
    {
        return columnQueryBuilder.addConstraint( new NotNullConstraintDefinition() );
    }

    public ColumnQueryBuilder primaryKey()
    {
        return columnQueryBuilder.addConstraint( new PrimaryKeyConstraintDefinition() );
    }

    public ColumnQueryBuilder foreignKey( String foreignTable, String foreignColumn )
    {
        return columnQueryBuilder.addConstraint( new ForeignKeyConstraintDefinition( foreignTable, foreignColumn ) );
    }
}
