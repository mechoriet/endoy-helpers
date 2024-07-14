package dev.endoy.helpers.common;

import dev.endoy.helpers.common.configuration.ConfigurationManager;
import dev.endoy.helpers.common.injector.Injector;
import dev.endoy.helpers.common.task.TaskManager;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.util.logging.Logger;

public abstract class EndoyApplication
{

    @Getter
    private final ConfigurationManager configurationManager;

    @Getter
    private final Logger logger;

    public EndoyApplication()
    {
        this.configurationManager = new ConfigurationManager( this );
        this.logger = setLogger();
    }

    public abstract TaskManager getTaskManager();

    public abstract File getDataFolder();

    public abstract Injector getInjector();

    public abstract void registerListeners( Object listenersInstance );

    public abstract Logger setLogger();

    public void registerDefaultInjectables()
    {
        this.getInjector().registerInjectable( ConfigurationManager.class, this.configurationManager );
        this.getInjector().registerInjectable( TaskManager.class, this.getTaskManager() );
        this.getInjector().registerInjectable( EndoyApplication.class, this );
    }
}
