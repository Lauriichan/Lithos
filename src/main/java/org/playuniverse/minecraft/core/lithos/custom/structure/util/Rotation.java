package org.playuniverse.minecraft.core.lithos.custom.structure.util;

import org.bukkit.block.structure.StructureRotation;

public enum Rotation {

    NORTH("WEST", "EAST", "SOUTH"),
    EAST("NORTH", "SOUTH", "WEST"),
    SOUTH("EAST", "WEST", "NORTH"),
    WEST("SOUTH", "NORTH", "EAST");

    private final Rotation left, right, mid;

    private Rotation(String left, String right, String mid) {
        this.left = Rotation.valueOf(left);
        this.right = Rotation.valueOf(right);
        this.mid = Rotation.valueOf(mid);
    }

    public Rotation rotate(int amount) {
        switch (amount % 4) {
        case 1:
            return right;
        case 2:
            return mid;
        case 3:
            return left;
        default:
            return this;
        }
    }

    public int rotateTo(Rotation rotation) {
        if (rotation == this) {
            return 0;
        }
        if (rotation == right) {
            return 1;
        }
        if (rotation == mid) {
            return 2;
        }
        return 3;
    }

    public static Rotation fromBukkit(StructureRotation rotation) {
        switch (rotation) {
        case CLOCKWISE_90:
            return Rotation.EAST;
        case CLOCKWISE_180:
            return Rotation.SOUTH;
        case COUNTERCLOCKWISE_90:
            return Rotation.WEST;
        default:
            return Rotation.NORTH;
        }
    }

    public static Rotation fromString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ignore) {
            return NORTH;
        }
    }

}
