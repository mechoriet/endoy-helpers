package dev.endoy.minecraft.helpers.database.definitions;

public class PrimaryKeyConstraintDefinition implements ConstraintDefinition
{
    @Override
    public boolean isInline()
    {
        return true;
    }

    @Override
    public String buildConstraint( String currentColumn )
    {
        return "PRIMARY KEY";
    }
}
