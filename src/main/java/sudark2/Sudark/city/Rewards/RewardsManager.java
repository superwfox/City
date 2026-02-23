package sudark2.Sudark.city.Rewards;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sudark2.Sudark.city.Util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

import static sudark2.Sudark.city.FileManager.Rewards;

public class RewardsManager {

    public static void showRewards(Player pl, Material type, int page) {
        if (!type.name().endsWith("SHULKER_BOX")) {
            pl.sendMessage("[§eCity§f] 仅支持潜影盒作为奖励箱");
            return;
        }
        pl.openInventory(getRewards(type, page));
    }

    private static final int INVENTORY_SIZE = 54;
    public static final String TITLE_Template = "战利品 | §7";

    public static Inventory getRewards(Material type, int page) {
        List<ItemStack> rewards = Rewards.getOrDefault(type, new ArrayList<>());

        int totalLength = rewards.size();
        int startIndex;
        if (page == -1) {
            int pageIndeed = totalLength / INVENTORY_SIZE;
            startIndex = pageIndeed * INVENTORY_SIZE;
        } else {
            startIndex = page * INVENTORY_SIZE;
        }
        int endIndex = Math.min(startIndex + INVENTORY_SIZE, totalLength);
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, TITLE_Template + type + " " + startIndex);

        for (int i = startIndex; i < endIndex; i++) {
            int slot = i - startIndex;
            inv.setItem(slot, rewards.get(i));
        }
        return inv;
    }

    public static void getRewardsList(Player pl) {

        StringBuilder list = new StringBuilder();
        MethodUtil.forEachYml(m -> {
            list.append("§e " + m + " §f:\n");
            List<ItemStack> rewards = Rewards.get(m);
            for (int i = 0; i < rewards.size(); i++) {
                ItemStack item = rewards.get(i);
                list.append(item.getType().name()).append(" x ").append(item.getAmount()).append(" \n");
            }
            list.append("=".repeat(16));
        });

        pl.sendMessage(list.toString());
    }

}
