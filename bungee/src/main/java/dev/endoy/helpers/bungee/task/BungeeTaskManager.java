package dev.endoy.helpers.bungee.task;

import dev.endoy.helpers.common.task.TaskManager;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BungeeTaskManager implements TaskManager
{

    private final Plugin plugin;

    @Override
    public int runTask( Runnable runnable, boolean async )
    {
        return ProxyServer.getInstance().getScheduler().runAsync( this.plugin, runnable ).getId();
    }

    @Override
    public int runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
    {
        return ProxyServer.getInstance().getScheduler().schedule( this.plugin, runnable, delay, timeUnit ).getId();
    }

    @Override
    public int runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
    {
        return ProxyServer.getInstance().getScheduler().schedule( this.plugin, runnable, delay, period, timeUnit ).getId();
    }

    @Override
    public void cancelTask( int taskId )
    {
        ProxyServer.getInstance().getScheduler().cancel( taskId );
    }
}
