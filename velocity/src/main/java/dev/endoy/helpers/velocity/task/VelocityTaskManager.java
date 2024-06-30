package dev.endoy.helpers.velocity.task;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.endoy.helpers.common.task.TaskManager;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class VelocityTaskManager implements TaskManager
{

    private final Object plugin;
    private final ProxyServer server;

    @Override
    public VelocityScheduledTask runTask( Runnable runnable, boolean async )
    {
        return new VelocityScheduledTask( this.server.getScheduler().buildTask( plugin, runnable ).schedule() );
    }

    @Override
    public VelocityScheduledTask runTaskLater( Runnable runnable, boolean async, long delay, TimeUnit timeUnit )
    {
        return new VelocityScheduledTask( this.server.getScheduler().buildTask( plugin, runnable ).delay( delay, timeUnit ).schedule() );
    }

    @Override
    public VelocityScheduledTask runTaskTimer( Runnable runnable, boolean async, long delay, long period, TimeUnit timeUnit )
    {
        return new VelocityScheduledTask( this.server.getScheduler().buildTask( plugin, runnable ).repeat( period, timeUnit ).schedule() );
    }
}
