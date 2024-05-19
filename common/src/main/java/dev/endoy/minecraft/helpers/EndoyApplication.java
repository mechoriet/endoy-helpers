package dev.endoy.minecraft.helpers;

import dev.endoy.minecraft.helpers.configuration.ConfigurationManager;
import dev.endoy.minecraft.helpers.task.TaskManager;
import lombok.Getter;

import java.io.File;

public abstract class EndoyApplication
{

    @Getter
    private static EndoyApplication instance = null;
    @Getter
    private final ConfigurationManager configurationManager;

    public EndoyApplication()
    {
        if ( instance != null )
        {
            throw new IllegalStateException( "EndoyApplication instance already exists" );
        }
        instance = this;

        this.configurationManager = new ConfigurationManager();
    }

    static void setInstance( EndoyApplication instance )
    {
        EndoyApplication.instance = instance;
    }

    public abstract TaskManager getTaskManager();

    public abstract File getDataFolder();
}
