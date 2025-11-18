package sudark2.Sudark.city.Rewards;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.*;

import static sudark2.Sudark.city.City.cityName;

public class ChunkLoadListener implements Listener {

    public static Map<String, int[]> chestLocs = new HashMap<>();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (!e.getWorld().getName().equals(cityName)) return;

        Chunk chunk = e.getChunk();

        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        String chunkCode = chunkX + "," + chunkZ;
        if (chestLocs.containsKey(chunkCode)) {

            int[] chestLoc = chestLocs.get(chunkCode);

            Block block = chunk.getBlock(chestLoc[0], chestLoc[1], chestLoc[2]);
            block.setType(Material.CHEST, false);
        }
    }

}
