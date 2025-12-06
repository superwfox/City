package sudark2.Sudark.city.Portal;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static sudark2.Sudark.city.City.*;
import static sudark2.Sudark.city.World.SecureZone.posPairs;

public class PortalManager implements Listener {

    @EventHandler
    public void onPlayerPortal(EntityPortalEvent e) {
        Entity entity = e.getEntity();
        String worldName = entity.getWorld().getName();

        if (worldName.equals(cityName)) {
            Bukkit.getScheduler().runTaskLater(get(), () -> entity.teleport(getMainWorld().getSpawnLocation()), 1);
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
        Bukkit.getScheduler().runTaskLater(get(), () -> entity.teleport(loc), 1);
    }

    @EventHandler
    public void onEntityPortal(PlayerPortalEvent e) {
        Player pl = e.getPlayer();
        String worldName = pl.getWorld().getName();

        if (worldName.equals(cityName)) {
            Bukkit.getScheduler().runTaskLater(get(), () -> pl.teleport(getMainWorld().getSpawnLocation()), 1);
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
        Bukkit.getScheduler().runTaskLater(get(), () -> pl.teleport(loc), 1);
    }

    static ConcurrentHashMap<String, BukkitTask> tasks = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerTeleportEvent e) {
        String worldName = e.getTo().getWorld().getName();
        Player pl = e.getPlayer();

        if (worldName.equals(templateName)) addTitle(pl);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        if (pl.getWorld().getName().equals(templateName)) addTitle(pl);
    }

    public static void addTitle(Player pl) {
        if (tasks.containsKey(pl.getName())) return;
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                pl.sendActionBar("§7[模板世界]");
                if (!pl.getWorld().getName().equals(templateName)) {
                    pl.sendActionBar("§e");
                    tasks.get(pl.getName()).cancel();
                    tasks.remove(pl.getName());
                }
            }
        }.runTaskTimerAsynchronously(get(), 0, 40);
        tasks.put(pl.getName(), task);
    }

}
