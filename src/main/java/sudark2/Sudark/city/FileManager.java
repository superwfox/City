package sudark2.Sudark.city;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sudark2.Sudark.city.City.get;

public class FileManager {

    public static File folder = get().getDataFolder();
    public static File saveZone = new File(folder, "saveZone.txt");
    public static File rewardsFile = new File(folder, "rewards.txt");

    public static void checkFile() {
        if (!folder.exists()) folder.mkdir();


        if (!saveZone.exists()) {
            try {
                saveZone.createNewFile();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    public static List<int[]> readSaveZones() {
        List<int[]> list = new ArrayList<>();

        try (BufferedReader r = new BufferedReader(new FileReader(saveZone))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] posPair = line.split("-");
                int[] intPair = Arrays.stream(posPair)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                list.add(intPair);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取安全区域文件时发生 IO 错误！");
        }

        return list;
    }

    public static void writeSaveZones(List<int[]> list) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(saveZone))) {
            for (int[] pair : list) {
                w.write(pair[0] + "-" + pair[1]);
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入安全区域文件时发生 IO 错误！");
        }
    }

    public static void writeRewards(ItemStack[] rewards) {
        String base64;
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)
        ) {
            dataOutput.writeInt(rewards.length);

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

    public static ItemStack[] readRewards() {
        String base64Data = "";

        try {
            base64Data = Files.readString(rewardsFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取战利品文件失败！");
        }

        if (base64Data.isEmpty()) return new ItemStack[0];

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64Data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)
        ) {
            int size = dataInput.readInt();
            ItemStack[] rewards = new ItemStack[size];

            for (int i = 0; i < size; i++) {
                rewards[i] = (ItemStack) dataInput.readObject();
            }

            return rewards;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("无法将 Base64 反序列化为 ItemStack[] 数组！", e);
        }
    }

}
