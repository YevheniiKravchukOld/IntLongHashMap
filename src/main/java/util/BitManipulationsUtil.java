package main.java.util;

public class BitManipulationsUtil {

    public static int getLongHigher32bit(long l) {
        return (int) (l >> 32);
    }

    public static int getLongLower32bit(long l) {
        return (int) l;
    }

    public static long setLongHigher32bit(long l, int i) {
        int lo = getLongLower32bit(l);
        return concatIntBits(i, lo);
    }

    public static long setLongLower32bit(long l, int i) {
        int hi = getLongHigher32bit(l);
        return concatIntBits(hi, i);
    }

    public static long concatIntBits(int hi, int lo) {
        return (((long) hi) << 32) | (lo & 0xffffffffL);
    }
}