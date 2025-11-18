package sudark2.Sudark.city;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static sudark2.Sudark.city.City.get;
import static sudark2.Sudark.city.Rewards.ChunkLoadListener.chestLocs;
import static sudark2.Sudark.city.World.SecureZone.posPairs;

public class FileManager {

    public static File folder = get().getDataFolder();
    public static File saveZone = new File(folder, "saveZone.txt");
    public static File rewardsFile = new File(folder, "rewards.txt");
    public static File rewardsChestFile = new File(folder, "chestLocs.txt");
    public static File configFile = new File(folder, "config.yml");

    public static int Percentage = 50;
    public static int LimitedNum = 20;
    public static List<ItemStack> Rewards = new ArrayList<>();

    public static void checkFile() {
        System.out.println("已重载配置文件");
        if (!folder.exists()) folder.mkdir();

        checkFileAndCreate(saveZone);
        checkFileAndCreate(rewardsFile);
        checkFileAndCreate(rewardsChestFile);
        createConfig();

        loadConfig();
        loadChestLocs();
        loadRewards();
        loadSaveZones();
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

    public static void createConfig() {
        FileConfiguration config = new YamlConfiguration();

        config.set("奖励箱概率.概率值", 500);
        config.set("奖励箱概率.类型", "正整数 [1-1000]");
        config.set("奖励箱概率.作用", "控制奖励箱每个槽位有多大概率刷出物品");
        config.set("奖励箱概率.计算公式", " 概率值 / 1000");

        config.set("城市维度.玩家个人刷怪上限", 20);
        config.set("城市维度.类型", "正整数");
        config.set("城市维度.作用", "仅在城市维度生效，根据玩家数量动态调整僵尸生成阈值");

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadConfig() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        Percentage = config.getInt("奖励箱概率.概率值");
        LimitedNum = config.getInt("城市维度.刷怪上限");

        if (Percentage < 1 || Percentage > 1000) {
            Percentage = 500;
            get().getLogger().warning("配置的奖励箱概率值不合法，已重置为默认值500");
        }

        if (LimitedNum < 1) {
            LimitedNum = 20;
            get().getLogger().warning("配置的城市维度刷怪上限不合法，已重置为默认值20");
        }

    }

    public static void loadChestLocs() {
        try (BufferedReader r = new BufferedReader(new FileReader(rewardsChestFile))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] posPair = line.split(",");
                if (posPair.length == 5) {
                    posPair = Arrays.stream(posPair)
                            .filter(s -> !s.isEmpty())
                            .toArray(String[]::new);

                    int[] intPair = Arrays.stream(posPair)
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    chestLocs.put(intPair[0] + "," + intPair[1], new int[]{intPair[2], intPair[3], intPair[4]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取奖励箱文件时发生 IO 错误！");
        }
    }

    public static void writeChestLocs() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(rewardsChestFile))) {
            for (String pair : chestLocs.keySet()) {
                w.write(pair + "," + chestLocs.get(pair)[0] + "," + chestLocs.get(pair)[1] + "," + chestLocs.get(pair)[2]);
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入奖励箱文件时发生 IO 错误！");
        }
    }

    public static void loadSaveZones() {
        try (BufferedReader r = new BufferedReader(new FileReader(saveZone))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] posPair = line.split(",");
                int[] intPair = Arrays.stream(posPair)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                posPairs.add(intPair);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取安全区域文件时发生 IO 错误！");
        }

    }

    public static void writeSaveZones(List<int[]> list) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(saveZone))) {
            for (int[] pair : list) {
                w.write(pair[0] + "," + pair[1]);
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入安全区域文件时发生 IO 错误！");
        }
    }

    public static void writeRewards(List<ItemStack> rewards) {
        String base64;
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)
        ) {
            dataOutput.writeInt(rewards.size());

            for (ItemStack item : rewards) {
                dataOutput.writeObject(item);
            }

            base64 = Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("战利品序列化失败！");
            return;
        }

        try {
            Files.writeString(rewardsFile.toPath(), base64);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("战利品写入文件失败！");
        }
    }

    public static void loadRewards() {
        String base64Data = "";

        try {
            base64Data = Files.readString(rewardsFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取战利品文件失败！");
        }

        if (base64Data.isEmpty()) return;

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64Data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)
        ) {
            int size = dataInput.readInt();

            for (int i = 0; i < size; i++) {
                Rewards.add((ItemStack) dataInput.readObject());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("无法将 Base64 反序列化为 List<ItemStack> 列表！", e);
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
