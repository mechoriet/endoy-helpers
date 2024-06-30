package dev.endoy.helpers.common.transform;

public interface ValueTransformer<T>
{

    T transformFromConfigValue( Object value );

    Object transformToConfigValue( T value );

}
