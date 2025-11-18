package sudark2.Sudark.city.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    private static final List<String> SUB_COMMANDS = List.of(
            "cancel",
            "save",
            "rewards",
            "check",
            "back",
            "list",
            "add",
            "remove",
            "allchest",
            "reload"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {

            String input = args[0].toLowerCase();

            for (String subCommand : SUB_COMMANDS) {
                if (subCommand.startsWith(input)) {
                    completions.add(subCommand);
                }
            }
            return completions;

        }

        if (args.length == 2 && args[0].equalsIgnoreCase("rewards")) {
            completions.add("[page:int:可选]");
        }

        return List.of();
    }
}