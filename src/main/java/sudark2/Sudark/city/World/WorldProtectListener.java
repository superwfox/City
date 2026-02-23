package sudark2.Sudark.city.World;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static sudark2.Sudark.city.City.*;
import static sudark2.Sudark.city.FileManager.denyBlocks;
import static sudark2.Sudark.city.Util.ChunkUtil.toChunkKey;
import static sudark2.Sudark.city.World.SecureZone.*;

public class WorldProtectListener implements Listener {

    @EventHandler
    public void onWorldProtectEvent(BlockBreakEvent event) {
        String worldName = event.getPlayer().getWorld().getName();
        if (!worldName.equals(cityName)) return;
        if (event.getPlayer().isOp()) return;
        Location loc = event.getBlock().getLocation();
        Chunk chunk = loc.getChunk();
        if (posPairs.contains(toChunkKey(chunk.getX(), chunk.getZ()))) event.setCancelled(true);
        if (shouldProtect(loc)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSetSpawn(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK) return;
        if (!block.getType().name().endsWith("BED")) return;

        String worldName = block.getLocation().getWorld().getName();
        if (worldName.equals(cityName)) event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        World world = event.getEntity().getWorld();
        if (!world.getName().equals(cityName)) return;

        Chunk ck = event.getLocation().getChunk();
        long key = toChunkKey(ck.getX(), ck.getZ());
        if (posPairs.contains(key)) {
            event.setCancelled(true);
        }
    }

    boolean shouldProtect(Location loc) {
        Material blockType = loc.getBlock().getType();

        if (denyBlocks.contains(blockType)) {
            Location newLoc = loc.clone();
            newLoc.setWorld(templateWorld);
            if (blockType == newLoc.getBlock().getType()) {
                return true;
            }
        }

        return false;
    }
}
