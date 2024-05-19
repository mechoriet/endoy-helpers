package dev.endoy.minecraft.helpers.spigot.task;

import dev.endoy.minecraft.helpers.task.TaskManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpigotTaskManager implements TaskManager
{

    private final Plugin plugin;

    @Override
    public int runTask( Runnable runnable, boolean async )
    {
        if ( async )
        {
            return Bukkit.getScheduler().runTaskAsynchronously( this.plugin, runnable ).getTaskId();
        }
        else
        {
            return Bukkit.getScheduler().runTask( this.plugin, runnable ).getTaskId();
        }
    }

    @Override
    public int runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
    {
        if ( async )
        {
            return Bukkit.getScheduler().runTaskLaterAsynchronously(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit )
            ).getTaskId();
        }
        else
        {
            return Bukkit.getScheduler().runTaskLater(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit )
            ).getTaskId();
        }
    }

    @Override
    public int runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
    {
        if ( async )
        {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit ),
                this.convertToTicks( period, timeUnit )
            ).getTaskId();
        }
        else
        {
            return Bukkit.getScheduler().runTaskTimer(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit ),
                this.convertToTicks( period, timeUnit )
            ).getTaskId();
        }
    }

    @Override
    public void cancelTask( int taskId )
    {
        Bukkit.getScheduler().cancelTask( taskId );
    }

    private Long convertToTicks( long delay, TimeUnit timeUnit )
    {
        return timeUnit.toMillis( delay ) / 50;
    }
}
