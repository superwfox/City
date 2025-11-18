package sudark2.Sudark.city.Rewards;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
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

import static sudark2.Sudark.city.FileManager.Rewards;
import static sudark2.Sudark.city.Rewards.ChunkLoadListener.chestLocs;
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
        if (bl == null || bl.getType() != Material.CHEST) return;

        int[][] locs = getLocFromMap(bl);

        String chunkCode = locs[0][0] +"," + locs[0][1];
        if (chestLocs.containsKey(chunkCode) && equal3(chestLocs.get(chunkCode), locs[1])) {

            opened.putIfAbsent(name,new HashSet<>());

            if (opened.get(name).contains(chunkCode)) return;

            opened.get(name).add(chunkCode);

            BlockState state = bl.getState();
            if (state instanceof Chest chest) {
                Inventory inv = chest.getBlockInventory();
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
