package dev.endoy.minecraft.helpers;

import dev.endoy.minecraft.helpers.configuration.ConfigurationManager;
import dev.endoy.minecraft.helpers.task.TaskManager;
import lombok.Getter;

import java.io.File;

public abstract class EndoyApplication
{

    @Getter
    private final ConfigurationManager configurationManager;

    public EndoyApplication()
    {
        this.configurationManager = new ConfigurationManager( this );
    }

    public abstract TaskManager getTaskManager();

    public abstract File getDataFolder();
}
