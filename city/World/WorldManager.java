package sudark2.Sudark.city.World;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

import static sudark2.Sudark.city.City.*;
import static sudark2.Sudark.city.World.SecureZone.posPairs;
import static sudark2.Sudark.city.World.WorldGenerator.createVoidWorld;
import static sudark2.Sudark.city.World.WorldGenerator.createWorld;
import static sudark2.Sudark.city.command.CityCommand.locs;

public class WorldManager {

    static int templateX = 1200;
    static int templateZ = 1200;

    public static void checkWorld() {
        if (Bukkit.getWorld(templateName) == null) {
            createVoidWorld(templateName);
            templateWorld = Bukkit.getWorld(templateName);
            createWorld(cityName);
        }
    }

    public static void resetWorld() {
        Bukkit.getOnlinePlayers().forEach(pl -> pl.sendMessage("§7城市世界重置中..."));

        World world = Bukkit.getWorld(cityName);
        if (world != null) {
            saveWorld(world);
            world.getPlayers().forEach(pl -> pl.kickPlayer("城市正在重置 请稍后重连"));
        }
        try {
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), cityName));
            createWorld(cityName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveWorld(World world) {
        for (int[] posPair : posPairs) {
            SecureZone.transferChunks(posPair, world);
        }
        Bukkit.getWorld(templateName).save();
    }

    public static Location getReasonableLocation(Player pl) {
        Location loc = pl.getLocation();
        World world = Bukkit.getWorld(templateName);
        locs.put(pl.getName(), loc);
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int tx = x % templateX;
        int tz = z % templateZ;
        int ty = world.getHighestBlockYAt(tx, tz);
        return new Location(world, tx, ty, tz);
    }

}
