package sudark2.Sudark.city;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RewardsManager {

    public static void showRewards(Player pl, int page) {
        pl.openInventory(RewardsManager.getRewards(page));
    }

    private static final int INVENTORY_SIZE = 54;
    private static final String TITLE = " 维度战利品管理|§0§lREWARDS";

    public static Inventory getRewards(int page) {
        ItemStack[] items = FileManager.readRewards();
        int totalLength = items.length;
        int startIndex;

        if (page == -1) {
            int pageIndeed = totalLength / INVENTORY_SIZE;
            startIndex = pageIndeed * INVENTORY_SIZE;
        } else {
            startIndex = page * INVENTORY_SIZE;
        }

        int endIndex = Math.min(startIndex + INVENTORY_SIZE, totalLength);


        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, TITLE);

        for (int i = startIndex; i < endIndex; i++) {
            inv.addItem(items[i]);
        }

        return inv;
    }

}
