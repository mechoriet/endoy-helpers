package dev.endoy.helpers.spigot.logging;

import dev.endoy.helpers.common.logger.EndoyLogging;
import dev.endoy.helpers.spigot.SpigotEndoyApplication;

public class SpigotLogging extends EndoyLogging
{

    public SpigotLogging( SpigotEndoyApplication application )
    {
        super( application );
        this.pluginName = "[" + application.getPlugin().getDescription().getName() + "] ";
        this.setParent( application.getPlugin().getLogger() );
    }
}
