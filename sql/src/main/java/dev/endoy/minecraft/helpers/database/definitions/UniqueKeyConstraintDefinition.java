package dev.endoy.minecraft.helpers.database.definitions;

public class UniqueKeyConstraintDefinition implements ConstraintDefinition
{

    @Override
    public boolean isInline()
    {
        return true;
    }

    @Override
    public String buildConstraint( String currentColumn )
    {
        return "UNIQUE";
    }
}
