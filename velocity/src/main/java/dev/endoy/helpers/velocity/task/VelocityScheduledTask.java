package dev.endoy.helpers.velocity.task;

import dev.endoy.helpers.common.task.ScheduledTask;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VelocityScheduledTask implements ScheduledTask
{

    private final com.velocitypowered.api.scheduler.ScheduledTask velocityTask;

    @Override
    public void cancel()
    {
        this.velocityTask.cancel();
    }
}
