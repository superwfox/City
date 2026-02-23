package sudark2.Sudark.city.Rewards;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static sudark2.Sudark.city.City.cityName;
import static sudark2.Sudark.city.City.templateName;
import static sudark2.Sudark.city.File.RewardsRelatedFiles.getReward;
import static sudark2.Sudark.city.File.RewardsRelatedFiles.writeRewards;
import static sudark2.Sudark.city.FileManager.*;
import static sudark2.Sudark.city.Rewards.RewardsManager.*;

public class RewardsListener implements Listener {

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        String title = e.getView().getTitle();
        if (!title.startsWith(TITLE_Template)) return;

        String[] things = title.split(" ");
        Material type = Material.valueOf(things[2].substring(2));

        List<ItemStack> rewards = Rewards.getOrDefault(type, new ArrayList<>());
        int index = Integer.parseInt(things[3]);

        ItemStack[] items = e.getInventory().getContents();
        int size = rewards.size();

        for (int i = items.length - 1 + index; i >= index; i--) {
            ItemStack item = items[i - index];

            if (item == null) {
                if (i >= size) continue;
                rewards.remove(i);
            } else {
                if (i >= size) {
                    rewards.add(item);
                    continue;
                }
                rewards.set(i, item);
            }
        }

        writeRewards(type, rewards);

        Player pl = (Player) e.getPlayer();
        pl.sendMessage("[§eCity§f] 战利品类型 " + type + " 已保存");
    }

    public static ConcurrentHashMap<String, Set<String>> opened = new ConcurrentHashMap<>();

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        Block bl = e.getClickedBlock();
        if (bl == null) return;
        if (!bl.getType().name().endsWith("SHULKER_BOX")) return;
        Player pl = e.getPlayer();
        if (!pl.getWorld().getName().equals(cityName)) return;

        if (!checkValid(bl, pl)) return;
        recordCheck(bl.getLocation().toString(), pl);

        List<ItemStack> rewards = getReward(bl.getType());
        int percentage = RewardsPercentage.get(bl.getType());

        if (rewards.isEmpty()) return;

        BlockState state = bl.getState();
        if (state instanceof Container container) {
            Inventory inv = container.getInventory();
            Random rand = new Random();

            for (int i = 0; i < 27; i++) {
                ItemStack item = rand.nextInt(1000) < percentage
                        ? rewards.get(rand.nextInt(rewards.size())).clone()
                        : null;
                inv.setItem(i, item);
            }
        }
    }

    private boolean checkValid(Block bl, Player pl) {
        if (opened.containsKey(pl.getName()) && opened.get(pl.getName()).contains(bl.getLocation().toString()))
            return false;
        Location checkLoc = bl.getLocation();
        checkLoc.setWorld(Bukkit.getWorld(templateName));
        return bl.getType() == checkLoc.getBlock().getType();
    }

    private void recordCheck(String chestCode, Player pl) {
        String name = pl.getName();
        opened.putIfAbsent(name, new HashSet<>());
        if (opened.get(name).contains(chestCode)) return;
        opened.get(name).add(chestCode);
    }

}
