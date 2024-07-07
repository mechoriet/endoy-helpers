package dev.endoy.helpers.spigot;

import dev.endoy.helpers.common.EndoyApplication;
import dev.endoy.helpers.common.injector.Injector;
import dev.endoy.helpers.common.task.TaskManager;
import dev.endoy.helpers.spigot.task.SpigotTaskManager;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SpigotEndoyApplication extends EndoyApplication
{

    private final Class<?> currentClass;
    @Getter
    private final Injector injector;
    private final SpigotTaskManager spigotTaskManager;
    @Getter
    private final Plugin plugin;

    private SpigotEndoyApplication( Plugin plugin, Class<?> clazz )
    {
        super();

        this.plugin = plugin;
        this.currentClass = clazz;
        this.spigotTaskManager = new SpigotTaskManager( plugin );

        this.injector = Injector.forProject( this.currentClass, this );
        this.registerDefaultInjectables();
    }

    public static SpigotEndoyApplication forPlugin( Plugin plugin )
    {
        return new SpigotEndoyApplication( plugin, plugin.getClass() );
    }

    @Override
    public TaskManager getTaskManager()
    {
        return this.spigotTaskManager;
    }

    @Override
    public File getDataFolder()
    {
        return this.plugin.getDataFolder();
    }

    @Override
    public void registerListeners( Object listenersInstance )
    {
        this.plugin.getServer().getPluginManager().registerEvents( (Listener) listenersInstance, this.plugin );
    }

    @Override
    public void registerDefaultInjectables()
    {
        super.registerDefaultInjectables();

        this.injector.registerInjectable( SpigotEndoyApplication.class, this );
        this.injector.registerInjectable( Injector.class, this.injector );
        this.injector.registerInjectable( plugin.getClass(), plugin );
    }

    public void inject() {
        this.injector.inject();
    }
}
