package dev.endoy.minecraft.helpers.database.definitions;

public class NotNullConstraintDefinition implements ConstraintDefinition
{

    @Override
    public boolean isInline()
    {
        return true;
    }

    @Override
    public String buildConstraint( String currentColumn )
    {
        return "NOT NULL";
    }
}
