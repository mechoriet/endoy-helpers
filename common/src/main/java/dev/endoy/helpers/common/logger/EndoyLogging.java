package dev.endoy.helpers.common.logger;

import dev.endoy.helpers.common.EndoyApplication;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public abstract class EndoyLogging extends Logger
{

    protected String pluginName;

    protected EndoyLogging( EndoyApplication application )
    {
        super( application.getClass().getCanonicalName(), null );
    }

    public void log( LogRecord logRecord )
    {
        logRecord.setMessage( this.pluginName + logRecord.getMessage() );
        super.log( logRecord );
    }
}
