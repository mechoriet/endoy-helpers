package dev.endoy.helpers.spigot.command;

import dev.endoy.helpers.common.command.CommandManager;
import dev.endoy.helpers.common.utils.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SpigotCommandManager implements CommandManager<SpigotCommand, SpigotTabComplete>
{

    private static final Field commandMap;

    static
    {
        commandMap = ReflectionUtils.getField( Bukkit.getServer().getClass(), "commandMap" );
    }

    private final Plugin plugin;

    @Override
    @SneakyThrows
    public void registerCommand( String command,
                                 List<String> aliases,
                                 String permission,
                                 SpigotCommand simpleCommand,
                                 @Nullable SpigotTabComplete tabComplete )
    {
        // TODO: implement

        CommandMap map = (CommandMap) commandMap.get( Bukkit.getServer() );

        map.register( command, "endoy:", new CommandHolder(
            simpleCommand,
            tabComplete
        ) );
    }


    @EventHandler
    public void onCommandCreate( final CommandCreateEvent event )
    {
        final SpigotCommand command = event.getCommand();

        try
        {
            unregisterCommands( map, command.getName(), command.getAliases() );


            CentrixCore.i().getLogger().info( "Registered " + command.getName() + " command!" );
        }
        catch ( Exception e )
        {
            CentrixCore.i().getLogger().warning( "Could not register " + command.getName() + " command!" );
            e.printStackTrace();
        }
    }

    @SuppressWarnings( "unchecked" )
    private void unregisterCommands( final CommandMap map, final String command, final List<String> aliases )
    {
        try
        {
            final Field field;
            if ( ReflectionUtils.hasField( map.getClass(), "knownCommands" ) )
            {
                field = ReflectionUtils.getField( map.getClass(), "knownCommands" );
            }
            else
            {
                field = ReflectionUtils.getField( map.getClass().getSuperclass(), "knownCommands" );
            }
            final Map<String, Command> commands = (Map<String, Command>) field.get( map );

            commands.remove( command );

            for ( String alias : aliases )
            {
                commands.remove( alias );
            }

            field.set( map, commands );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
