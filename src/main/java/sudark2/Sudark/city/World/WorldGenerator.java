package sudark2.Sudark.city.World;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static sudark2.Sudark.city.City.*;


public class WorldGenerator extends ChunkGenerator {

    public static void createVoidWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(World.Environment.NORMAL)
                .type(WorldType.FLAT)
                .generateStructures(false)
                .generator(new VoidChunkGenerator());
        creator.createWorld();
    }

    public static void createWorld(String worldName) {
        createVoidWorld(worldName);
        World world = Bukkit.getWorld(worldName);
        Bukkit.unloadWorld(world, true);

        try {
            Thread.sleep(2000);  // 延迟2秒，确保世界彻底卸载
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File folder = Bukkit.getWorldContainer();

        File cityWorld = new File(folder, worldName + "/region");
        File templateWorld = new File(folder, templateName + "/region");

        try {
            if (cityWorld.exists()) {
                FileUtils.deleteDirectory(cityWorld);
            }
            if (templateWorld.exists()) {
                FileUtils.copyDirectory(templateWorld, cityWorld);
            }
            createVoidWorld(worldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class VoidChunkGenerator extends ChunkGenerator {
        @Override
        public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}


