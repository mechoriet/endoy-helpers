package dev.endoy.minecraft.helpers.injector;

public class FailedInjectionException extends RuntimeException
{

    public FailedInjectionException( String message )
    {
        super( message );
    }

    public FailedInjectionException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public FailedInjectionException( Throwable cause )
    {
        super( cause );
    }
}
