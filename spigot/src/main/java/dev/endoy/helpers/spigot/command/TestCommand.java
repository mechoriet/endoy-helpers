package dev.endoy.helpers.spigot.command;

import dev.endoy.helpers.common.injector.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

// TODO: remove
@Command( command = "test", aliases = { "t" }, permission = "endoy-helpers.test" )
public class TestCommand implements SpigotCommand, SpigotTabComplete
{

    @Override
    public void onCommand( CommandSender commandSender, List<String> args )
    {
        commandSender.sendMessage( "This is a test command :)" );
    }

    @Override
    public List<String> onTabComplete( CommandSender commandSender, List<String> args )
    {
        return List.of(
            "hello", "world"
        );
    }
}
