package sudark2.Sudark.city.command;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sudark2.Sudark.city.FileManager;
import sudark2.Sudark.city.Rewards.RewardsManager;

import java.util.concurrent.ConcurrentHashMap;

import static sudark2.Sudark.city.City.getMainWorld;
import static sudark2.Sudark.city.World.SecureZone.posPairs;
import static sudark2.Sudark.city.World.WorldManager.resetWorld;
import static sudark2.Sudark.city.Rewards.RewardsManager.showRewards;
import static sudark2.Sudark.city.World.WorldManager.getReasonableLocation;

public class CityCommand implements CommandExecutor {

    public static ConcurrentHashMap<String, Location> locs = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player pl)) return false;

        switch (args[0]) {
            case "cancel" -> removeChunkFromPairs(pl);
            case "save" -> addChunkToPairs(pl);
            case "rewards" -> showRewards(pl, args.length > 1 ? Integer.parseInt(args[1]) : 0);
            case "check" -> pl.teleport(getReasonableLocation(pl));
            case "back" -> pl.teleport(locs.get(pl.getName()) == null ? (pl.getBedSpawnLocation() == null ? getMainWorld().getSpawnLocation() : pl.getBedLocation()) : locs.get(pl.getName()));
            case "list" -> RewardsManager.getRewardsList(pl);
            case "add" -> RewardsManager.add(pl);
            case "remove" -> RewardsManager.remove(pl);
            case "allchest" -> RewardsManager.getAllChest(pl);
            case "reload" -> resetWorld();
        }
        return true;
    }

    private void removeChunkFromPairs(Player pl) {
        Chunk chunk = pl.getChunk();
        int cx = chunk.getX();
        int cz = chunk.getZ();

        for (int[] posPair : posPairs) {
            if (posPair[0] == cx && posPair[1] == cz) {
                posPairs.remove(posPair);
                FileManager.writeSaveZones(posPairs);
                pl.sendMessage("[§eCity§f] 从安全区域移除 区块[" + cx + "," + cz + "]");
                return;
            }
        }
    }

    private void addChunkToPairs(Player pl) {
        Chunk chunk = pl.getLocation().getChunk();
        int cx = chunk.getX();
        int cz = chunk.getZ();

        posPairs.add(new int[]{cx, cz});
        FileManager.writeSaveZones(posPairs);
        pl.sendMessage("[§eCity§f] 添加 区块[" + cx + "," + cz + "] 到安全区域");
    }


}
