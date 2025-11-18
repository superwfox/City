package sudark2.Sudark.city.Portal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Random;

import static sudark2.Sudark.city.City.*;
import static sudark2.Sudark.city.World.SecureZone.posPairs;

public class PortalManager implements Listener {

    @EventHandler
    public void onPlayerPortal(EntityPortalEvent e) {
        Entity entity = e.getEntity();
        String worldName = entity.getWorld().getName();

        if (worldName.equals(cityName)) {
            Bukkit.getScheduler().runTaskLater(get(),()->entity.teleport(getMainWorld().getSpawnLocation()),1);
            return;
        }
        int[] chunkLoc;
        if (posPairs.isEmpty()) chunkLoc = new int[]{0, 0};
        else
            chunkLoc = posPairs.get(new Random().nextInt(posPairs.size()));
        int x = chunkLoc[0] * 16 + 8;
        int z = chunkLoc[1] * 16 + 8;
        int y = Bukkit.getWorld(cityName).getHighestBlockYAt(x, z);
        Location loc = new Location(Bukkit.getWorld(cityName), x, y + 1, z);
        entity.teleport(loc);
    }

    @EventHandler
    public void onEntityPortal(PlayerPortalEvent e) {
        Player pl = e.getPlayer();
        String worldName = pl.getWorld().getName();

        if (worldName.equals(cityName)) {
            Bukkit.getScheduler().runTaskLater(get(),()->pl.teleport(getMainWorld().getSpawnLocation()),1);
            return;
        }
        int[] chunkLoc;
        if (posPairs.isEmpty()) chunkLoc = new int[]{0, 0};
        else
            chunkLoc = posPairs.get(new Random().nextInt(posPairs.size()));
        int x = chunkLoc[0] * 16 + 8;
        int z = chunkLoc[1] * 16 + 8;
        int y = Bukkit.getWorld(cityName).getHighestBlockYAt(x, z);
        Location loc = new Location(Bukkit.getWorld(cityName), x, y + 1, z);
        Bukkit.getScheduler().runTaskLater(get(),()->pl.teleport(loc),1);
    }

}
