package dev.endoy.helpers.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.endoy.helpers.common.EndoyApplication;
import dev.endoy.helpers.common.injector.Injector;
import dev.endoy.helpers.common.task.TaskManager;
import dev.endoy.helpers.velocity.task.VelocityTaskManager;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;

public class VelocityEndoyApplication extends EndoyApplication
{

    private final Class<?> currentClass;
    @Getter
    private final Injector injector;
    private final VelocityTaskManager velocityTaskManager;
    @Getter
    private final Object plugin;
    @Getter
    private final File dataFolder;
    @Getter
    private final ProxyServer proxyServer;

    private VelocityEndoyApplication( Object plugin,
                                      Class<?> clazz,
                                      ProxyServer proxyServer,
                                      File dataFolder )
    {
        super();

        this.plugin = plugin;
        this.currentClass = clazz;
        this.velocityTaskManager = new VelocityTaskManager( plugin, proxyServer );
        this.dataFolder = dataFolder;
        this.proxyServer = proxyServer;

        this.injector = Injector.forProject( this.currentClass, this );
        this.registerDefaultInjectables();
    }

    public static VelocityEndoyApplication forPlugin( Object plugin, ProxyServer proxyServer, File dataFolder )
    {
        return new VelocityEndoyApplication(
            plugin,
            plugin.getClass(),
            proxyServer,
            dataFolder
        );
    }

    @Override
    public TaskManager getTaskManager()
    {
        return this.velocityTaskManager;
    }

    @Override
    public void registerListeners( Object listenersInstance )
    {

        if ( Arrays.stream( listenersInstance.getClass().getDeclaredMethods() )
            .noneMatch( method -> method.isAnnotationPresent( Subscribe.class ) ) )
        {
            System.out.println( "Class " + listenersInstance.getClass().getName() + " was skipped as it does not have any methods with @Subscribe annotation." );
            return;
        }
        this.proxyServer.getEventManager().register( this.plugin, listenersInstance );
    }

    @Override
    public void registerDefaultInjectables()
    {
        super.registerDefaultInjectables();

        this.injector.registerInjectable( VelocityEndoyApplication.class, this );
        this.injector.registerInjectable( Injector.class, this.injector );
        this.injector.registerInjectable( plugin.getClass(), plugin );
        this.injector.registerInjectable( ProxyServer.class, this.proxyServer );
    }

    public void inject() {
        this.injector.inject();
    }
}
