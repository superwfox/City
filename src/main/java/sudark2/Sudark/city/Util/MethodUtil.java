package sudark2.Sudark.city.Util;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

import static sudark2.Sudark.city.FileManager.rewardsFiles;

public class MethodUtil {

    private MethodUtil() {
    }

    public static void forEachYml(Consumer<String> handler) {
        for (File f : Objects.requireNonNull(rewardsFiles.listFiles())) {
            if (!f.isFile()) continue;
            String name = f.getName();
            if (!name.endsWith(".yml")) continue; // 只处理 yml

            String header = name.substring(0, name.length() - 4); // 去掉末尾 .yml
            handler.accept(header);
        }
    }
}
