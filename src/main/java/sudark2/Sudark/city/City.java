package sudark2.Sudark.city;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sudark2.Sudark.city.command.CityCommand;
import sudark2.Sudark.city.command.CommandTabCompleter;

public final class City extends JavaPlugin {

    public static String cityName = "City-World";
    public static String templateName = "Template-World";

    @Override
    public void onEnable() {

        PluginCommand cmd = getCommand("city");
        assert cmd != null;
        cmd.setExecutor(new CityCommand());
        cmd.setTabCompleter(new CommandTabCompleter());


    }

    @Override
    public void onDisable() {
    }

    public static Plugin get(){
        return JavaPlugin.getPlugin(City.class);
    }
}
