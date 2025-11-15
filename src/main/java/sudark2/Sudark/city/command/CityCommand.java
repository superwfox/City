package sudark2.Sudark.city.command;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sudark2.Sudark.city.SecureZone;

import java.util.concurrent.ConcurrentHashMap;

import static sudark2.Sudark.city.RewardsManager.showRewards;
import static sudark2.Sudark.city.WorldManager.getReasonableLocation;

public class CityCommand implements CommandExecutor {

    public static ConcurrentHashMap<String, Location> locs = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player pl)) return false;

        switch (args[0]) {
            case "cancel" -> removeChunkFromPairs(pl);
            case "save" -> addChunkToPairs(pl);
            case "rewards" -> showRewards(pl, args.length > 1 ? 0 : Integer.parseInt(args[1]));
            case "check" -> pl.teleport(getReasonableLocation(pl));
            case "back" ->
                    pl.teleport(locs.get(pl.getName()) == null ? (pl.getBedSpawnLocation() == null ? Bukkit.getWorlds().getFirst().getSpawnLocation() : pl.getBedLocation()) : locs.get(pl.getName()));
        }
        return false;
    }

    private void removeChunkFromPairs(Player pl) {
        Chunk chunk = pl.getChunk();
        int cx = chunk.getX();
        int cz = chunk.getZ();

        for (int[] posPair : SecureZone.posPairs) {
            if (posPair[0] == cx && posPair[1] == cz) {
                SecureZone.posPairs.remove(posPair);
                pl.sendMessage("[§eCity§f] 从安全区域移除 区块[" + cx + "," + cz + "]");
                return;
            }
        }
    }

    private void addChunkToPairs(Player pl) {
        Chunk chunk = pl.getChunk();
        int cx = chunk.getX();
        int cz = chunk.getZ();

        SecureZone.posPairs.add(new int[]{cx, cz});
        pl.sendMessage("[§eCity§f] 添加 区块[" + cx + "," + cz + "] 到安全区域");
    }


}
