package org.playuniverse.minecraft.core.lithos.util;

public final class BitHelper {

    private BitHelper() {}

    public static int merge7Bit(int value) {
        return (((value < 0 ? 1 : 0) & 0b1) << 0 | (Math.abs(value) & 0x3F) << 1);
    }

    public static int unmerge7Bit(int value) {
        int out = (value >> 1) & 0x3F;
        return ((value >> 0) & 0b1) == 1 ? out * -1 : out;
    }

    public static int merge11Bit(int value) {
        return (((value < 0 ? 1 : 0) & 0b1) << 0 | (Math.abs(value) & 0x3FF) << 1);
    }

    public static int unmerge11Bit(int value) {
        int out = (value >> 1) & 0x3FF;
        return ((value >> 0) & 0b1) == 1 ? out * -1 : out;
    }

}
