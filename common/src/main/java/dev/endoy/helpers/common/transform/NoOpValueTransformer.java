package dev.endoy.helpers.common.transform;

public class NoOpValueTransformer implements ValueTransformer<String>
{

    @Override
    public String transformFromConfigValue( Object value )
    {
        return String.valueOf( value );
    }

    @Override
    public Object transformToConfigValue( String value )
    {
        return value;
    }
}
