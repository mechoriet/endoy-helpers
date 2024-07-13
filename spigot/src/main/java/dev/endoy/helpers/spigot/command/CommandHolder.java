package dev.endoy.helpers.spigot.command;

import dev.endoy.bungeeutilisalsx.common.BuX;
import dev.endoy.bungeeutilisalsx.common.api.user.interfaces.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.Command;

public class CommandHolder extends Command
{

    private final Command command;

    public CommandHolder( final Command command )
    {
        super( command.getName(), "", command.getAliases() );

        this.command = command;
    }

    @Override
    public void execute( final CommandSender sender, final String[] args )
    {
        this.command.execute( this.getUser( sender ), args );
    }

    @Override
    public Iterable<String> onTabComplete( final CommandSender sender, final String[] args )
    {
        return this.command.onTabComplete( this.getUser( sender ), args );
    }

    private User getUser( final CommandSender sender )
    {
        return sender instanceof ProxiedPlayer
            ? BuX.getApi().getUser( sender.getName() ).orElse( null )
            : BuX.getApi().getConsoleUser();
    }
}
