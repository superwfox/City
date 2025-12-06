package sudark2.Sudark.city.World;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.util.Random;

import static sudark2.Sudark.city.City.*;


public class WorldGenerator extends ChunkGenerator {

    public static void createVoidWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(World.Environment.NORMAL).type(WorldType.FLAT).generateStructures(false).generator(new VoidChunkGenerator());
        creator.createWorld();
    }

    public static void createWorld() {
        World world = Bukkit.getWorld(cityName);
        if (world != null) Bukkit.unloadWorld(world, false);
        else createVoidWorld(cityName);

        World template = Bukkit.getWorld(templateName);
        if (template != null) template.save();

        File folder = Bukkit.getWorldContainer();

        File cityWorld = new File(folder, cityName + "/region");
        File templateWorld = new File(folder, templateName + "/region");

        File cityEntities = new File(folder, cityName + "/entities");
        File templateEntities = new File(folder, templateName + "/entities");

        try {
            if (cityWorld.exists()) FileUtils.deleteDirectory(cityWorld);
            if (cityEntities.exists()) FileUtils.deleteDirectory(cityEntities);

            if (templateWorld.exists()) FileUtils.copyDirectory(templateWorld, cityWorld);
            if (templateEntities.exists()) FileUtils.copyDirectory(templateEntities, cityEntities);

            createVoidWorld(cityName);
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


