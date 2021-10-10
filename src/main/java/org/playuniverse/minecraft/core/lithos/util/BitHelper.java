package org.playuniverse.minecraft.core.lithos.util;

public final class BitHelper {

    private BitHelper() {}

    public static int merge7Bit(final int value) {
        return ((value < 0 ? 1 : 0) & 0b1) << 0 | (Math.abs(value) & 0x3F) << 1;
    }

    public static int unmerge7Bit(final int value) {
        final int out = value >> 1 & 0x3F;
        return (value >> 0 & 0b1) == 1 ? out * -1 : out;
    }

    public static int merge11BitUnsigned(final int value) {
        return (Math.abs(value) & 0x7FF) << 0;
    }

    public static int unmerge11BitUnsigned(final int value) {
        return value >> 0 & 0x7FF;
    }

}
