package sudark2.Sudark.city.Rewards;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sudark2.Sudark.city.FileManager;

import java.util.*;

import static sudark2.Sudark.city.City.cityName;
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


//    public static void add(Player pl) {
//        Block target = pl.getTargetBlockExact(6);
//        if (target == null) {
//            pl.sendMessage("[§eCity§f] 请对准一个方块");
//            return;
//        }
//
//        target.breakNaturally();
//        target.setType(Material.CHEST);
//        target.getWorld().spawnParticle(
//                Particle.DUST_COLOR_TRANSITION,
//                target.getLocation(),
//                6, 0.1, 0.1, 0.1, 0,
//                new Particle.DustTransition(Color.YELLOW, Color.ORANGE, 2.2f)
//        );
//
//        int[][] loc = getLocFromMap(target);
//        String chunkCode = loc[0][0] + "," + loc[0][1];
//        int[] existing = chestLocs.get(chunkCode);
//
//        if (existing != null && equal3(existing, loc[1])) {
//            pl.sendMessage("[§eCity§f] 此处已存在奖励箱");
//            return;
//        }
//
//        if (existing != null) pl.sendMessage("[§eCity§f] 已覆盖该区块的旧奖励箱");
//        else pl.sendMessage("[§eCity§f] 奖励箱已添加");
//
//        chestLocs.put(chunkCode, loc[1]);
//        FileManager.writeChestLocs();
//    }

//    public static void remove(Player pl) {
//        Block target = pl.getTargetBlockExact(6);
//        if (target == null) {
//            pl.sendMessage("[§eCity§f] 请对准一个方块");
//            return;
//        }
//
//        int[][] loc = getLocFromMap(target);
//        String chunkCode = loc[0][0] + "," + loc[0][1];
//
//        if (chestLocs.containsKey(chunkCode) && equal3(chestLocs.get(chunkCode), loc[1])) {
//            target.breakNaturally(new ItemStack(Material.AIR));
//            chestLocs.remove(chunkCode);
//            FileManager.writeChestLocs();
//            pl.sendMessage("[§eCity§f] 奖励箱已移除");
//            return;
//        }
//
//        pl.sendMessage("[§eCity§f] 请对准一个奖励箱");
//
//    }
//
//    public static boolean equal3(int[] a, int[] b) {
//        return a[0] == b[0] && a[1] == b[1] && a[2] == b[2];
//    }

//    public static void getAllChest(Player pl) {
//        Location pLoc = pl.getLocation();
//        int playerChunkX = pLoc.getBlockX() >> 4;
//        int playerChunkZ = pLoc.getBlockZ() >> 4;
//
//        List<Map.Entry<String, int[]>> list = new ArrayList<>(chestLocs.entrySet());
//
//        list.sort(Comparator.comparingInt((Map.Entry<String, int[]> e) -> {
//            String[] parts = e.getKey().split(",");
//            int cx = Integer.parseInt(parts[0]);
//            int cz = Integer.parseInt(parts[1]);
//            return Math.abs(cx - playerChunkX) + Math.abs(cz - playerChunkZ);
//        }).reversed()); // 距离越大越靠前
//
//        ComponentBuilder builder = new ComponentBuilder("§e 奖励箱§f列表：");
//        int index = 0;
//
//        for (Map.Entry<String, int[]> entry : list) {
//            index++;
//            String chunkCode = entry.getKey();
//            int[] cloc = entry.getValue();
//
//            String[] parts = chunkCode.split(",");
//            int cx = Integer.parseInt(parts[0]);
//            int cz = Integer.parseInt(parts[1]);
//            int dist = Math.abs(cx - playerChunkX) + Math.abs(cz - playerChunkZ);
//
//            int[] loc = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
//            int x = loc[0] * 16 + cloc[0];
//            int y = cloc[1];
//            int z = loc[1] * 16 + cloc[2];
//
//            builder.append("\n " + index + ". (" + chunkCode + ") §e[" + x + "," + y + "," + z + "]§r")
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
//                            new ComponentBuilder("§e[点击传送至此位置]\n§7 》 距离: §f" + dist + " §7个区块").create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
//                            "/execute in " + cityName.toLowerCase() + " as @s run tp " + x + " " + (y + 1) + " " + z));
//        }
//
//        BaseComponent[] msg = builder.create();
//        pl.spigot().sendMessage(msg);
//        pl.sendMessage("[§eCity§f] 共有§e§l " + index + "§r 个奖励箱");
//    }

//    public static int[][] getLocFromMap(Block bl) {
//        Chunk chunk = bl.getChunk();
//        int chunkX = chunk.getX();
//        int chunkZ = chunk.getZ();
//        int cx = bl.getX() & 0xF; // 等价于 x % 16
//        int cy = bl.getY();
//        int cz = bl.getZ() & 0xF; // 等价于 z % 16
//        return new int[][]{{chunkX, chunkZ}, {cx, cy, cz}};
//    }

}
