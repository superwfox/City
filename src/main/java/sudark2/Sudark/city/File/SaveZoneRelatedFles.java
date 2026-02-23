package sudark2.Sudark.city.File;

import java.io.*;

import static sudark2.Sudark.city.FileManager.saveZone;
import static sudark2.Sudark.city.World.SecureZone.posPairs;

public class SaveZoneRelatedFles {

    public static void loadSaveZones() {
        posPairs.clear();
        if (!saveZone.exists() || saveZone.length() == 0) return;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(saveZone))) {
            while (dis.available() >= 8) {
                posPairs.add(dis.readLong());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取安全区域文件时发生 IO 错误！");
        }
    }

    public static void writeSaveZones() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveZone))) {
            for (long key : posPairs) {
                dos.writeLong(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入安全区域文件时发生 IO 错误！");
        }
    }
}
