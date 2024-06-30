package dev.endoy.helpers.common;

import dev.endoy.helpers.common.task.ScheduledTask;
import dev.endoy.helpers.common.task.TaskManager;
import dev.endoy.helpers.common.injector.Injector;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Getter
public class EndoyApplicationTest extends EndoyApplication
{

    private File dataFolder;
    @Getter
    @Setter
    private Injector injector;

    public EndoyApplicationTest()
    {
    }

    @BeforeEach
    public void setUp() throws IOException
    {
        this.dataFolder = Files.createTempDirectory( "endoy-helpers" ).toFile();
    }

    @AfterEach
    public void tearDown()
    {
        this.dataFolder.delete();
    }

    @Override
    public TaskManager getTaskManager()
    {
        return new TaskManager()
        {
            @Override
            public ScheduledTask runTask( Runnable runnable, boolean async )
            {
                return () ->
                {
                };
            }

            @Override
            public ScheduledTask runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
            {
                return () ->
                {
                };
            }

            @Override
            public ScheduledTask runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
            {
                return () ->
                {
                };
            }
        };
    }

    @Override
    public void registerListeners( Object listenersInstance )
    {
    }
}
