package sudark2.Sudark.city.Rewards;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sudark2.Sudark.city.FileManager;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static sudark2.Sudark.city.City.cityName;
import static sudark2.Sudark.city.City.templateName;
import static sudark2.Sudark.city.FileManager.Rewards;
import static sudark2.Sudark.city.Rewards.RewardsManager.*;

public class RewardsListener implements Listener {

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        String title = e.getView().getTitle();

        if (!title.startsWith(TITLE)) return;

        int index = Integer.parseInt(title.split(" ")[1]);

        ItemStack[] items = e.getInventory().getContents();
        for (ItemStack item : items) {
            if (item != null) {
                System.out.println(index + " " + item + " " + Rewards.size());
                if (index < Rewards.size())
                    Rewards.set(index, item);
                else Rewards.add(item);
            }
            index++;
        }
        FileManager.writeRewards(Rewards);

        Player pl = (Player) e.getPlayer();
        pl.sendMessage("[§eCity§f] 战利品已保存");
    }

    public static ConcurrentHashMap<String, Set<String>> opened = new ConcurrentHashMap<>();

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        String name = pl.getName();
        Block bl = e.getClickedBlock();

        if (!pl.getWorld().getName().equals(cityName)) return;

        if (bl == null) return;
        Material m = bl.getType();
        if (m != Material.CHEST && m != Material.BARREL) return;

        Location checkLoc = bl.getLocation();
        checkLoc.setWorld(Bukkit.getWorld(templateName));
        String chestCode = checkLoc.toString();

        if (checkLoc.getBlock().getType() == m) {
            opened.putIfAbsent(name, new HashSet<>());

            if (opened.get(name).contains(chestCode)) return;

            opened.get(name).add(chestCode);

            BlockState state = bl.getState();
            if (state instanceof Container container) {
                Inventory inv = container.getInventory();
                Random rand = new Random();

                if (Rewards.isEmpty()) return;
                for (int i = 0; i < inv.getSize(); i++) {
                    ItemStack item = rand.nextInt(1000) < FileManager.Percentage
                            ? Rewards.get(rand.nextInt(Rewards.size()))
                            : null;
                    inv.setItem(i, item);
                }
            }

        }

    }

}
