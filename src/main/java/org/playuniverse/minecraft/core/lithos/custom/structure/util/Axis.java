package org.playuniverse.minecraft.core.lithos.custom.structure.util;

public enum Axis {

    X,
    Y,
    Z;

    public Axis rotate(int amount) {
        if (Y == this) {
            return this;
        }
        return amount % 2 == 0 ? this : X == this ? Z : X;
    }

    public static Axis fromString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ignore) {
            return Y;
        }
    }

}
