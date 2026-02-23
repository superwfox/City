package sudark2.Sudark.city.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import sudark2.Sudark.city.Util.MethodUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static sudark2.Sudark.city.FileManager.*;

public class RewardsRelatedFiles {

    private RewardsRelatedFiles() {
    }

    public static void createConfig() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        if (!config.getKeys(false).isEmpty()) return;

        MethodUtil.forEachYml((header) -> {
            config.set(header + ".奖励箱概率.概率值", 500);
            config.set(header + ".奖励箱概率.类型", "正整数 [1-1000]");
            config.set(header + ".奖励箱概率.作用", "控制奖励箱每个槽位有多大概率刷出物品");
            config.set(header + ".奖励箱概率.计算公式", "概率值 / 1000");
        });

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void loadConfig() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        MethodUtil.forEachYml((header) -> {
            RewardsPercentage.put(Material.valueOf(header), config.getInt(header + ".奖励箱概率.概率值", 500));
        });
    }

    public static void writeRewards(Material type, List<ItemStack> rewards) {
        Rewards.put(type, rewards);
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

        File des = new File(rewardsFiles, type.name() + ".yml");
        try {
            if (!des.exists()) des.createNewFile();
            Files.writeString(des.toPath(), base64);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("战利品写入文件失败！");
        }
    }

    public static void readRewards() {

        MethodUtil.forEachYml((header) -> {
            Rewards.put(Material.valueOf(header), getReward(Material.valueOf(header)));
        });

    }

    public static List<ItemStack> getReward(Material type) {
        List<ItemStack> rewards = new ArrayList<>();
        String base64Data = "";
        File des = new File(rewardsFiles, type.name() + ".yml");
        try {
            base64Data = Files.readString(des.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取战利品文件失败！");
        }

        if (base64Data.isBlank()) return rewards;

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64Data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)
        ) {
            int size = dataInput.readInt();
            for (int i = 0; i < size; i++) {
                rewards.add((ItemStack) dataInput.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("战利品" + type.name() + "反序列化失败，可能文件损坏");
        }
        return rewards;
    }
}
