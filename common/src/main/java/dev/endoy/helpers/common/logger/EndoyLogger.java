package dev.endoy.helpers.common.logger;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class EndoyLogger extends Logger
{

    public EndoyLogger( String name )
    {
        super( name, null );
    }

    public static Logger forClass( Class<?> clazz )
    {
        Logger logger = new EndoyLogger( clazz.getPackageName() );
        if ( !LogManager.getLogManager().addLogger( logger ) )
        {
            logger = LogManager.getLogManager().getLogger( clazz.getPackageName() );
        }

        return logger;
    }
}
