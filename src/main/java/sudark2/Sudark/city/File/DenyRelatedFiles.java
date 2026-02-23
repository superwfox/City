package sudark2.Sudark.city.File;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.*;

import static sudark2.Sudark.city.FileManager.denyBlocks;
import static sudark2.Sudark.city.FileManager.denyBlocksFile;

public class DenyRelatedFiles {

    public static void loadDenyBlocks() {
        denyBlocks.clear();
        try (BufferedReader r = new BufferedReader(new FileReader(denyBlocksFile))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    Material material = Material.valueOf(line);
                    denyBlocks.add(material);
                } catch (IllegalArgumentException e) {
                    System.err.println("无效的方块类型: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取禁止破坏方块文件时发生 IO 错误！");
        }
    }

    public static void writeDenyBlocks() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(denyBlocksFile))) {
            for (Material material : denyBlocks) {
                w.write(material.name());
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入禁止破坏方块文件时发生 IO 错误！");
        }
    }

    public static void addDenyBlock(Material material, Player pl) {
        try {
            if (denyBlocks.add(material)) {
                writeDenyBlocks();
                pl.sendMessage("[§eCity§f] 已添加 §6" + material.name() + " §f到禁止破坏列表");
            } else {
                pl.sendMessage("[§eCity§f] §6" + material.name() + " §f已在禁止破坏列表中");
            }
        } catch (IllegalArgumentException e) {
            pl.sendMessage("[§eCity§f] §c无效的方块类型: " + material);
        }

    }

    public static void removeDenyBlock(Material material, Player pl) {
        if (denyBlocks.remove(material)) {
            writeDenyBlocks();
            pl.sendMessage("[§eCity§f] 已从禁止破坏列表中移除 §6" + material.name());
        } else
            pl.sendMessage("[§eCity§f] §6" + material.name() + " §f不在禁止破坏列表中");
    }

}
