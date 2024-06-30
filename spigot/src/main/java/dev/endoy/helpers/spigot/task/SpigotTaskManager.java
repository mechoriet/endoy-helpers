package dev.endoy.helpers.spigot.task;

import dev.endoy.helpers.common.task.TaskManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpigotTaskManager implements TaskManager
{

    private final Plugin plugin;

    @Override
    public SpigotScheduledTask runTask( Runnable runnable, boolean async )
    {
        BukkitTask bukkitTask;

        if ( async )
        {
            bukkitTask = Bukkit.getScheduler().runTaskAsynchronously( this.plugin, runnable );
        }
        else
        {
            bukkitTask = Bukkit.getScheduler().runTask( this.plugin, runnable );
        }

        return new SpigotScheduledTask( bukkitTask.getTaskId() );
    }

    @Override
    public SpigotScheduledTask runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
    {
        BukkitTask bukkitTask;

        if ( async )
        {
            bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit )
            );
        }
        else
        {
            bukkitTask = Bukkit.getScheduler().runTaskLater(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit )
            );
        }

        return new SpigotScheduledTask( bukkitTask.getTaskId() );
    }

    @Override
    public SpigotScheduledTask runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
    {
        BukkitTask bukkitTask;

        if ( async )
        {
            bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit ),
                this.convertToTicks( period, timeUnit )
            );
        }
        else
        {
            bukkitTask = Bukkit.getScheduler().runTaskTimer(
                this.plugin,
                runnable,
                this.convertToTicks( delay, timeUnit ),
                this.convertToTicks( period, timeUnit )
            );
        }

        return new SpigotScheduledTask( bukkitTask.getTaskId() );
    }

    private Long convertToTicks( long delay, TimeUnit timeUnit )
    {
        return timeUnit.toMillis( delay ) / 50;
    }
}
