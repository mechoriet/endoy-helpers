package dev.endoy.helpers.spigot.task;

import dev.endoy.helpers.common.task.ScheduledTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class SpigotScheduledTask implements ScheduledTask
{

    private final int taskId;

    @Override
    public void cancel()
    {
        Bukkit.getScheduler().cancelTask( this.taskId );
    }
}
