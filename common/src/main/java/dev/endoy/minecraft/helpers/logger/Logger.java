package dev.endoy.minecraft.helpers.logger;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.slf4j.impl.SimpleLoggerFactory;

public class Logger
{

    private final org.slf4j.Logger logger;
    private final Class<?> currentClass;

    public Logger( Class<?> clazz )
    {
        this.currentClass = clazz;

        Thread currentThread = Thread.currentThread();
        ClassLoader previousClassLoader = currentThread.getContextClassLoader();

        try
        {
            // Temporarily set the context class loader to the Logger class loader so slf4j can find it's relocated classes
            currentThread.setContextClassLoader( SimpleLoggerFactory.class.getClassLoader() );
            this.logger = LoggerFactory.getLogger( clazz.getName() );
        }
        finally
        {
            currentThread.setContextClassLoader( previousClassLoader );
        }
    }

    public static Logger forClass( Class<?> clazz )
    {
        return new Logger( clazz );
    }

    public void error( String message )
    {
        if ( this.isAtLoggingLevel( Level.ERROR ) )
        {
            this.logger.error( message );
        }
    }

    public void error( String message, Throwable throwable )
    {
        if ( this.isAtLoggingLevel( Level.ERROR ) )
        {
            this.logger.error( message, throwable );
        }
    }

    public void error( String message, Object... args )
    {
        if ( this.isAtLoggingLevel( Level.ERROR ) )
        {
            this.logger.error( message, args );
        }
    }

    public void warn( String message )
    {
        if ( this.isAtLoggingLevel( Level.WARN ) )
        {
            this.logger.warn( message );
        }
    }

    public void warn( String message, Throwable throwable )
    {
        if ( this.isAtLoggingLevel( Level.WARN ) )
        {
            this.logger.warn( message, throwable );
        }
    }

    public void warn( String message, Object... args )
    {
        if ( this.isAtLoggingLevel( Level.WARN ) )
        {
            this.logger.warn( message, args );
        }
    }

    public void info( String message )
    {
        if ( this.isAtLoggingLevel( Level.INFO ) )
        {
            this.logger.info( message );
        }
    }

    public void info( String message, Throwable throwable )
    {
        if ( this.isAtLoggingLevel( Level.INFO ) )
        {
            this.logger.info( message, throwable );
        }
    }

    public void info( String message, Object... args )
    {
        if ( this.isAtLoggingLevel( Level.INFO ) )
        {
            this.logger.info( message, args );
        }
    }

    public void debug( String message )
    {
        if ( this.isAtLoggingLevel( Level.DEBUG ) )
        {
            this.logger.debug( message );
        }
    }

    public void debug( String message, Throwable throwable )
    {
        if ( this.isAtLoggingLevel( Level.DEBUG ) )
        {
            this.logger.debug( message, throwable );
        }
    }

    public void debug( String message, Object... args )
    {
        if ( this.isAtLoggingLevel( Level.DEBUG ) )
        {
            this.logger.debug( message, args );
        }
    }

    public void trace( String message )
    {
        if ( this.isAtLoggingLevel( Level.TRACE ) )
        {
            this.logger.trace( message );
        }
    }

    public void trace( String message, Throwable throwable )
    {
        if ( this.isAtLoggingLevel( Level.TRACE ) )
        {
            this.logger.trace( message, throwable );
        }
    }

    public void trace( String message, Object... args )
    {
        if ( this.isAtLoggingLevel( Level.TRACE ) )
        {
            this.logger.trace( message, args );
        }
    }

    private boolean isAtLoggingLevel( Level level )
    {
        return LoggingConfig.getLoggingLevel( this.currentClass ).toInt() <= level.toInt();
    }
}
