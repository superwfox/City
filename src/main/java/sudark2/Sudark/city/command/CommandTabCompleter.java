package sudark2.Sudark.city.command;

import com.sun.source.tree.LabeledStatementTree;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static sudark2.Sudark.city.FileManager.denyBlocks;

public class CommandTabCompleter implements TabCompleter {

    private static final List<String> SUB_COMMANDS = List.of(
            "cancel",
            "save",
            "rewards",
            "check",
            "back",
            "list",
            "reload",
            "denyBlock",
            "denyBlockList"
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

        if (args.length == 2 && args[0].equalsIgnoreCase("denyBlock")) {
            return List.of("add", "remove");
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("denyBlock") && args[1].equalsIgnoreCase("add")) {
            String input = args[2].toUpperCase();
            completions.addAll(
                    Arrays.stream(Material.values())
                            .filter(Material::isBlock)
                            .map(Material::name)
                            .filter(name -> name.startsWith(input))
                            .collect(Collectors.toList())
            );
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("denyBlock") && args[1].equalsIgnoreCase("remove")) {
            String input = args[2].toUpperCase();
            completions.addAll(
                    denyBlocks.stream()
                            .map(Material::name)
                            .filter(name -> name.startsWith(input))
                            .collect(Collectors.toList())
            );
        }

        return completions;
    }
}