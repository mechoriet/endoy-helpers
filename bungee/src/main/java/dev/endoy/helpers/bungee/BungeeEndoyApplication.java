package dev.endoy.helpers.bungee;

import dev.endoy.helpers.bungee.task.BungeeTaskManager;
import dev.endoy.helpers.common.EndoyApplication;
import dev.endoy.helpers.common.injector.Injector;
import dev.endoy.helpers.common.task.TaskManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class BungeeEndoyApplication extends EndoyApplication
{

    private final Class<?> currentClass;
    @Getter
    private final Injector injector;
    private final BungeeTaskManager bungeeTaskManager;
    @Getter
    private final Plugin plugin;

    private BungeeEndoyApplication( Plugin plugin, Class<?> clazz )
    {
        super();

        this.plugin = plugin;
        this.currentClass = clazz;
        this.bungeeTaskManager = new BungeeTaskManager( plugin );

        this.injector = Injector.forProject( this.currentClass, this );
        this.registerDefaultInjectables();
        this.injector.inject();
    }

    public static BungeeEndoyApplication forPlugin( Plugin plugin )
    {
        return new BungeeEndoyApplication( plugin, plugin.getClass() );
    }

    @Override
    public TaskManager getTaskManager()
    {
        return this.bungeeTaskManager;
    }

    @Override
    public File getDataFolder()
    {
        return this.plugin.getDataFolder();
    }

    @Override
    public void registerListeners( Object listenersInstance )
    {
        ProxyServer.getInstance().getPluginManager().registerListener( this.plugin, (Listener) listenersInstance );
    }

    @Override
    public void registerDefaultInjectables()
    {
        super.registerDefaultInjectables();

        this.injector.registerInjectable( BungeeEndoyApplication.class, this );
        this.injector.registerInjectable( Injector.class, this.injector );
        this.injector.registerInjectable( plugin.getClass(), plugin );
    }
}
