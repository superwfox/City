package sudark2.Sudark.city.World;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

import static sudark2.Sudark.city.City.templateName;

public class SecureZone {

    public static List<int[]> posPairs = new ArrayList<>();

    public static void transferChunks(int[] posPairs, World world) {
        int chunkX = posPairs[0];
        int chunkZ = posPairs[1];

        Chunk chunk = Bukkit.getWorld(templateName).getChunkAt(chunkX, chunkZ);
        Chunk srcChunk = world.getChunkAt(chunkX, chunkZ);

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = 0; y < 320; y++) {
                    BlockState state = srcChunk.getBlock(x, y, z).getState();
                    chunk.getBlock(x, y, z).setBlockData(state.getBlockData(), false);
                }
    }


}
