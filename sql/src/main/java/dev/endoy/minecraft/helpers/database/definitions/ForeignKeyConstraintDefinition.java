package dev.endoy.minecraft.helpers.database.definitions;

import lombok.Value;

@Value
public class ForeignKeyConstraintDefinition implements ConstraintDefinition
{

    String foreignTable;
    String foreignColumn;

    @Override
    public boolean isInline()
    {
        return false;
    }

    @Override
    public String buildConstraint( String currentColumn )
    {
        return String.format( "FOREIGN KEY (%s) REFERENCES %s(%s)", currentColumn, foreignTable, foreignColumn );
    }
}
