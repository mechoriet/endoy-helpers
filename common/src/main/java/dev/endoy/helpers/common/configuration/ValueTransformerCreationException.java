package dev.endoy.helpers.common.configuration;

public class ValueTransformerCreationException extends RuntimeException
{

    public ValueTransformerCreationException( String message )
    {
        super( message );
    }

    public ValueTransformerCreationException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public ValueTransformerCreationException( Throwable cause )
    {
        super( cause );
    }
}
