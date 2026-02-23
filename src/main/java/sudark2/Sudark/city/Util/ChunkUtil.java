package sudark2.Sudark.city.Util;

public class ChunkUtil {

    public static long toChunkKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    public static int getX(long key) {
        return (int) (key >> 32);
    }

    public static int getZ(long key) {
        return (int) key;
    }
}
