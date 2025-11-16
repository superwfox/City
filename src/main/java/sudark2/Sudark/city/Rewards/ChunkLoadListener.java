package sudark2.Sudark.city.Rewards;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import sudark2.Sudark.city.FileManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static sudark2.Sudark.city.FileManager.Rewards;

public class ChunkLoadListener implements Listener {

    public static Map<String, int[]> chestLocs = new HashMap<>();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if (e.isNewChunk()) {
            Chunk chunk = e.getChunk();

            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

            String chunkCode = chunkX + "," + chunkZ;

            if (chestLocs.containsKey(chunkCode)) {

                int[] chestLoc = chestLocs.get(chunkCode);

                Block block = chunk.getBlock(chestLoc[0], chestLoc[1], chestLoc[2]);
                block.setType(Material.CHEST, false);

                Chest chest = (Chest) block;
                Inventory inv = chest.getBlockInventory();
                Random rand = new Random();

                for (int i = 0; i < inv.getSize(); i++) {
                    inv.setItem(i, rand.nextInt(1000) < FileManager.Percentage ? Rewards.get(rand.nextInt(Rewards.size())) : null);
                }

            }

        }
    }

}
