package dev.endoy.minecraft.helpers.configuration;

public class InvalidConfigurationException extends RuntimeException
{

    public InvalidConfigurationException( String message )
    {
        super( message );
    }

    public InvalidConfigurationException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public InvalidConfigurationException( Throwable cause )
    {
        super( cause );
    }
}
