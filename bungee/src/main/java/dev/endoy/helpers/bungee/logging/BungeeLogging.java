package dev.endoy.helpers.bungee.logging;

import dev.endoy.helpers.bungee.BungeeEndoyApplication;
import dev.endoy.helpers.common.logger.EndoyLogging;

public class BungeeLogging extends EndoyLogging
{

    public BungeeLogging( BungeeEndoyApplication application )
    {
        super( application );
        this.pluginName = "[" + application.getPlugin().getDescription().getName() + "] ";
        this.setParent( application.getPlugin().getLogger() );
    }
}
