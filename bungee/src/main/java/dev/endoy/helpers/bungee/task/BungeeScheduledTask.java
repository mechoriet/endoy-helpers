package dev.endoy.helpers.bungee.task;

import dev.endoy.helpers.common.task.ScheduledTask;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;

@RequiredArgsConstructor
public class BungeeScheduledTask implements ScheduledTask
{

    private final int taskId;

    @Override
    public void cancel()
    {
        ProxyServer.getInstance().getScheduler().cancel( this.taskId );
    }
}
