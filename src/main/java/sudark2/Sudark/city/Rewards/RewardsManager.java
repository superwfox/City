package sudark2.Sudark.city.Rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sudark2.Sudark.city.FileManager;

import java.util.Arrays;

import static sudark2.Sudark.city.FileManager.Rewards;
import static sudark2.Sudark.city.Rewards.ChunkLoadListener.chestLocs;

public class RewardsManager {

    public static void showRewards(Player pl, int page) {
        pl.openInventory(RewardsManager.getRewards(page));
    }

    private static final int INVENTORY_SIZE = 54;
    public static final String TITLE = "战利品管理|§0§lREWARDS";

    public static Inventory getRewards(int page) {
        int totalLength = Rewards.size();
        int startIndex;
        if (page == -1) {
            int pageIndeed = totalLength / INVENTORY_SIZE;
            startIndex = pageIndeed * INVENTORY_SIZE;
        } else {
            startIndex = page * INVENTORY_SIZE;
        }
        int endIndex = Math.min(startIndex + INVENTORY_SIZE, totalLength);
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, TITLE + " " + startIndex);

        for (int i = startIndex; i < endIndex; i++) {
            int slot = i - startIndex;
            inv.setItem(slot, Rewards.get(i));
        }
        return inv;
    }


    public static void getRewardsList(Player pl) {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < Rewards.size(); i++) {
            ItemStack item = Rewards.get(i);
            list.append(item.getType().name()).append(" x ").append(item.getAmount()).append(" \n");
        }
        pl.sendMessage(list.toString());
    }


    public static void add(Player pl) {
        Block target = pl.getTargetBlockExact(6);
        if (target == null) {
            pl.sendMessage("[§eCity§f] 请对准一个方块");
            return;
        }

        target.breakNaturally();
        target.setType(Material.CHEST);
        target.getWorld().spawnParticle(
                Particle.DUST_COLOR_TRANSITION,
                target.getLocation(),
                6, 0.1, 0.1, 0.1, 0,
                new Particle.DustTransition(Color.YELLOW, Color.ORANGE, 2.2f)
        );

        int[][] loc = getLocFromMap(target);
        int[] existing = chestLocs.get(loc[0]);

        if (existing != null && Arrays.equals(existing, loc[1])) {
            pl.sendMessage("[§eCity§f] 此处已存在奖励箱");
            return;
        }

        if (existing != null) pl.sendMessage("[§eCity§f] 已覆盖该区块的旧奖励箱");
        else pl.sendMessage("[§eCity§f] 奖励箱已添加");

        chestLocs.put(loc[0][0] + "," + loc[0][1], loc[1]);
        FileManager.writeChestLocs();
    }

    public static void remove(Player pl) {
        Block target = pl.getTargetBlockExact(6);
        if (target == null) {
            pl.sendMessage("[§eCity§f] 请对准一个方块");
            return;
        }

        int[][] loc = getLocFromMap(target);

        if (chestLocs.containsKey(loc[0]) && Arrays.equals(chestLocs.get(loc[0]), loc[1])) {
            target.breakNaturally(new ItemStack(Material.AIR), false);
            chestLocs.remove(loc[0]);
            FileManager.writeChestLocs();
            pl.sendMessage("[§eCity§f] 奖励箱已移除");
            return;
        }

        pl.sendMessage("[§eCity§f] 请对准一个奖励箱");

    }

    public static void getAllChest(Player pl) {

        TextComponent msg = Component.text("§e奖励箱§f列表：");
        int index = 0;
        for (String chunkCode : chestLocs.keySet()) {
            index++;
            int[] cloc = chestLocs.get(chunkCode);
            int[] loc = Arrays.stream(chunkCode.split(",")).mapToInt(Integer::parseInt).toArray();
            msg = msg.append(
                    Component.text(index + ". (" + chunkCode + ") §e[" + (loc[0] * 16 + cloc[0]) + "," + cloc[1] + "," + (loc[1] * 16 + cloc[2]) + "]§r \n")
                            .hoverEvent(Component.text("[点击传送至此位置]"))
                            .clickEvent(ClickEvent.runCommand("/tp "
                                    + pl.getName() + " "
                                    + (loc[0] * 16 + cloc[0]) + " "
                                    + (cloc[1] + 1) + " "
                                    + (loc[1] * 16 + cloc[2])))
            );
        }
        pl.sendMessage(msg);
        pl.sendMessage("[§eCity§f] 共有§e§l " + index + "§r 个奖励箱");

    }


    public static int[][] getLocFromMap(Block bl) {
        Chunk chunk = bl.getChunk();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int cx = bl.getX() & 0xF; // 等价于 x % 16
        int cy = bl.getY();
        int cz = bl.getZ() & 0xF; // 等价于 z % 16
        return new int[][]{{chunkX, chunkZ}, {cx, cy, cz}};
    }

}
