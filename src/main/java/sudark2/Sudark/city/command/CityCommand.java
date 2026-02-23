package sudark2.Sudark.city.command;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sudark2.Sudark.city.Rewards.RewardsManager;

import java.util.concurrent.ConcurrentHashMap;

import static sudark2.Sudark.city.City.*;
import static sudark2.Sudark.city.File.DenyRelatedFiles.*;
import static sudark2.Sudark.city.File.SaveZoneRelatedFles.writeSaveZones;
import static sudark2.Sudark.city.FileManager.*;
import static sudark2.Sudark.city.Rewards.RewardsListener.opened;
import static sudark2.Sudark.city.Util.ChunkUtil.toChunkKey;
import static sudark2.Sudark.city.World.SecureZone.posPairs;
import static sudark2.Sudark.city.World.WorldManager.resetWorld;
import static sudark2.Sudark.city.Rewards.RewardsManager.showRewards;
import static sudark2.Sudark.city.World.WorldManager.getReasonableLocation;

public class CityCommand implements CommandExecutor {

    public static ConcurrentHashMap<String, Location> locs = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player pl) {

            if (args.length == 0) {
                pl.sendMessage("用法: /city <cancel|save|rewards|check|back|list|denyBlock|denyBlockList|reload>");
                return true;
            }

            switch (args[0]) {
                case "cancel" -> removeChunkFromPairs(pl);
                case "save" -> addChunkToPairs(pl);
                case "rewards" -> {
                    Material type = pl.getItemInHand().getType();
                    int page = args.length > 1 ? Integer.parseInt(args[1]) : 0;
                    showRewards(pl, type, page);
                }
                case "check" -> pl.teleport(getReasonableLocation(pl, templateName));
                case "back" ->
                        pl.teleport(locs.get(pl.getName()) == null ? (pl.getBedSpawnLocation() == null ? getMainWorld().getSpawnLocation() : pl.getBedLocation()) : locs.get(pl.getName()));
                case "list" -> RewardsManager.getRewardsList(pl);
                case "denyBlock" -> handleDenyBlock(pl, args);
                case "denyBlockList" -> showDenyBlockList(pl);
//                case "add" -> RewardsManager.add(pl);
//                case "remove" -> RewardsManager.remove(pl);
//                case "allchest" -> RewardsManager.getAllChest(pl);
                case "reload" -> {
                    resetWorld();
                    checkFile();
                    opened.clear();
                }
            }
        }

        if (sender instanceof
                CommandBlock cb) {
            if (args.length < 4) {
                System.out.println("[City] /city tp 后需要三个数字作为坐标");
                return false;
            }

            float x = Float.parseFloat(args[1]);
            float y = Float.parseFloat(args[2]);
            float z = Float.parseFloat(args[3]);

            cb.getLocation().getNearbyPlayers(6).forEach(pl -> pl.teleport(new Location(Bukkit.getWorld(cityName), x, y, z)));
        }


        return true;
    }

    private void removeChunkFromPairs(Player pl) {
        Chunk chunk = pl.getLocation().getChunk();
        int cx = chunk.getX();
        int cz = chunk.getZ();
        long key = toChunkKey(cx, cz);

        if (posPairs.remove(key)) {
            writeSaveZones();
            pl.sendMessage("[§eCity§f] 从安全区域移除 区块[" + cx + "," + cz + "]");
        } else {
            pl.sendMessage("[§eCity§f] 该区块不在安全区域中");
        }
    }

    private void addChunkToPairs(Player pl) {
        Chunk chunk = pl.getLocation().getChunk();
        int cx = chunk.getX();
        int cz = chunk.getZ();
        long key = toChunkKey(cx, cz);

        if (posPairs.add(key)) {
            writeSaveZones();
            pl.sendMessage("[§eCity§f] 添加 区块[" + cx + "," + cz + "] 到安全区域");
        } else {
            pl.sendMessage("[§eCity§f] 该区块已在安全区域中");
        }
    }

    private void handleDenyBlock(Player pl, String[] args) {
        if (args.length < 2) {
            pl.sendMessage("[§eCity§f] 用法: /city denyBlock <方块类型>");
            return;
        }
        String materialName = args[2].toUpperCase();
        switch (args[1].toLowerCase()) {
            case "add" -> addDenyBlock(Material.valueOf(materialName), pl);
            case "remove" -> removeDenyBlock(Material.valueOf(materialName), pl);
            default -> pl.sendMessage("[§eCity§f] 用法: /city denyBlock <add|remove> <方块类型>");
        }
    }

    private void showDenyBlockList(Player pl) {
        if (denyBlocks.isEmpty()) {
            pl.sendMessage("[§eCity§f] 禁止破坏列表为空");
            return;
        }

        pl.sendMessage("[§eCity§f] §l禁止破坏的方块列表:");
        int index = 0;
        for (Material material : denyBlocks) {
            String color = (index % 2 == 0) ? "§e" : "§6";
            pl.sendMessage(color + "  - " + material.name());
            index++;
        }
    }

}
