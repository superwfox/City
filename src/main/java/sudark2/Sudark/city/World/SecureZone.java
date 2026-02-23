package sudark2.Sudark.city.World;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import java.util.HashSet;
import java.util.Set;

import static sudark2.Sudark.city.City.templateName;
import static sudark2.Sudark.city.Util.ChunkUtil.getX;
import static sudark2.Sudark.city.Util.ChunkUtil.getZ;

public class SecureZone {

    public static Set<Long> posPairs = new HashSet<>();

    public static void transferChunks(long chunkKey, World world) {
        int chunkX = getX(chunkKey);
        int chunkZ = getZ(chunkKey);

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
