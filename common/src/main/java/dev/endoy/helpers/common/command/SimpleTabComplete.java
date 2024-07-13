package dev.endoy.helpers.common.command;

import java.util.List;

public interface SimpleTabComplete<CS>
{

    List<String> onTabComplete( CS commandSender, List<String> args );// TODO: make sure these are immutable

}
