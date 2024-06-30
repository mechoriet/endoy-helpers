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
    public BungeeScheduledTask runTask( Runnable runnable, boolean async )
    {
        return new BungeeScheduledTask( ProxyServer.getInstance().getScheduler().runAsync( this.plugin, runnable ).getId() );
    }

    @Override
    public BungeeScheduledTask runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
    {
        return new BungeeScheduledTask( ProxyServer.getInstance().getScheduler().schedule( this.plugin, runnable, delay, timeUnit ).getId() );
    }

    @Override
    public BungeeScheduledTask runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
    {
        return new BungeeScheduledTask( ProxyServer.getInstance().getScheduler().schedule( this.plugin, runnable, delay, period, timeUnit ).getId() );
    }
}
