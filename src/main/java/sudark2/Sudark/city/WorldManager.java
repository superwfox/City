package sudark2.Sudark.city;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static sudark2.Sudark.city.City.cityName;
import static sudark2.Sudark.city.command.CityCommand.locs;

public class WorldManager {

    static int templateX = 100;
    static int templateZ = 100;

    public static World resetWorld() throws IOException {
        deleteWorld();
        return createVoidWorld(cityName);
    }

    public static void deleteWorld() throws IOException {
        World world = Bukkit.getWorld(cityName);
        if (world != null) {
            saveWorld(world);
            Bukkit.unloadWorld(world, false);
        }
        FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), cityName));
    }

    private static void saveWorld(World world) {
        List<int[]> posPairs = FileManager.readSaveZones();
        for (int[] posPair : posPairs) {
            SecureZone.transferChunks(posPair, world);
        }
    }

    public static World createVoidWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);

        creator.environment(World.Environment.NORMAL)
                .type(WorldType.FLAT)
                .generateStructures(false)
                .generator(new VoidChunkGenerator());

        return creator.createWorld();
    }

    // 自定义虚空生成器
    private static class VoidChunkGenerator extends ChunkGenerator {
        @Override
        public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }

    public static Location getReasonableLocation(Player pl) {
        Location loc = locs.get(pl.getName());
        locs.put(pl.getName(), loc);
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int tx = x % templateX;
        int tz = z % templateZ;
        int ty = Bukkit.getWorld(cityName).getHighestBlockYAt(tx, tz);
        return new Location(Bukkit.getWorld(cityName), tx, ty, tz);
    }

}
