package dev.endoy.minecraft.helpers;

import dev.endoy.minecraft.helpers.injector.Injector;
import dev.endoy.minecraft.helpers.task.TaskManager;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

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
            public int runTask( Runnable runnable, boolean async )
            {
                return 0;
            }

            @Override
            public int runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
            {
                return 0;
            }

            @Override
            public int runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
            {
                return 0;
            }

            @Override
            public void cancelTask( int taskId )
            {

            }
        };
    }

    @Override
    public void registerListeners( Object listenersInstance )
    {
    }
}
