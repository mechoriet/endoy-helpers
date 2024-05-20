package dev.endoy.minecraft.helpers.database.definitions;

import lombok.Value;

public interface DefaultValues
{

    static LiteralDefaultValue literal( Object defaultValue )
    {
        return new LiteralDefaultValue( defaultValue );
    }

    static CurrentTimestampDefaultValue currentTimestamp()
    {
        return new CurrentTimestampDefaultValue();
    }

    @Value
    class LiteralDefaultValue implements DefaultValues
    {
        Object defaultValue;
    }

    @Value
    class CurrentTimestampDefaultValue implements DefaultValues
    {
    }
}
