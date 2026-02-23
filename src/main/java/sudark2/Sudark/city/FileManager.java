package sudark2.Sudark.city;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static sudark2.Sudark.city.City.get;
import static sudark2.Sudark.city.File.DenyRelatedFiles.loadDenyBlocks;
import static sudark2.Sudark.city.File.RewardsRelatedFiles.*;
import static sudark2.Sudark.city.File.SaveZoneRelatedFles.loadSaveZones;

public class FileManager {

    public static File folder = get().getDataFolder();
    public static File rewardsFiles = new File(folder, "rewardsList");

    public static File saveZone = new File(folder, "saveZone.data");
    public static File denyBlocksFile = new File(folder, "denyBlocks.txt");
    public static File configFile = new File(folder, "config.yml");

    public static ConcurrentHashMap<Material, List<ItemStack>> Rewards = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Material, Integer> RewardsPercentage = new ConcurrentHashMap<>();
    public static Set<Material> denyBlocks = new HashSet<>();

    public static void checkFile() {
        Bukkit.getLogger().info("已重载配置文件");

        if (!folder.exists()) folder.mkdir();
        if (!rewardsFiles.exists()) rewardsFiles.mkdir();

        checkFileAndCreate(saveZone);
        checkFileAndCreate(denyBlocksFile);
        createConfig();

        loadConfig();
        readRewards();
        loadSaveZones();
        loadDenyBlocks();
    }

    public static String getLevelName() {
        Properties properties = new Properties();
        File propertiesFile = new File("server.properties");

        try (FileReader reader = new FileReader(propertiesFile)) {
            properties.load(reader);
            return properties.getProperty("level-name");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkFileAndCreate(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
