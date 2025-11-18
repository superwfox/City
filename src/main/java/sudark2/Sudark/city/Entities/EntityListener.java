package sudark2.Sudark.city.Entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.util.BoundingBox;

import static sudark2.Sudark.city.City.cityName;
import static sudark2.Sudark.city.FileManager.LimitedNum;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Location spawnLoc = event.getLocation();

        if (!spawnLoc.getWorld().getName().equals(cityName)) {
            return;
        }

        if (!(event.getEntity() instanceof Zombie)) {
            return;
        }

        BoundingBox box = new BoundingBox(
                spawnLoc.getX() - 24, spawnLoc.getY() - 24, spawnLoc.getZ() - 24,
                spawnLoc.getX() + 24, spawnLoc.getY() + 24, spawnLoc.getZ() + 24
        );

        long entityCount = spawnLoc.getWorld().getNearbyEntities(box, entity -> true).size();

        if (entityCount >= LimitedNum) {
            event.setCancelled(true);
        }
    }
}
