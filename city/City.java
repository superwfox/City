package sudark2.Sudark.city;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sudark2.Sudark.city.Entities.EntityListener;
import sudark2.Sudark.city.Portal.PortalManager;
import sudark2.Sudark.city.Rewards.ChunkLoadListener;
import sudark2.Sudark.city.command.CityCommand;
import sudark2.Sudark.city.command.CommandTabCompleter;
import sudark2.Sudark.city.Rewards.RewardsListener;

import static sudark2.Sudark.city.Clock.start;
import static sudark2.Sudark.city.FileManager.getLevelName;
import static sudark2.Sudark.city.World.WorldManager.checkWorld;

public final class City extends JavaPlugin {

    public static String cityName = "City-World";
    public static String templateName = "Template-World";
    public static World templateWorld;

    @Override
    public void onEnable() {

        checkWorld();
        start();

        FileManager.checkFile();

        PluginCommand cmd = getCommand("city");
        {
            assert cmd != null;
            cmd.setExecutor(new CityCommand());
            cmd.setTabCompleter(new CommandTabCompleter());
        }

        Bukkit.getPluginManager().registerEvents(new RewardsListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkLoadListener(), this);
        Bukkit.getPluginManager().registerEvents(new PortalManager(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

    }

    public static Plugin get() {
        return JavaPlugin.getPlugin(City.class);
    }

    public static World getMainWorld() {
        return Bukkit.getWorld(getLevelName());
    }
}
