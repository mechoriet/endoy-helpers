package dev.endoy.minecraft.helpers.database.definitions;

public interface ConstraintDefinition
{

    boolean isInline();

    String buildConstraint( String column );

}
