package org.playuniverse.minecraft.core.lithos.custom.structure.util;

import org.bukkit.block.structure.StructureRotation;

public enum Rotation {

    NORTH(3, 1, 2),
    EAST(0, 2, 3),
    SOUTH(1, 3, 0),
    WEST(2, 0, 1);

    private final int left, right, mid;

    private Rotation(int left, int right, int mid) {
        this.left = left;
        this.right = right;
        this.mid = mid;
    }

    public Rotation rotate(int amount) {
        int id = switch (amount % 4) {
        case 1:
            yield right;
        case 2:
            yield mid;
        case 3:
            yield left;
        default:
            yield -1;
        };
        return id == -1 ? this : Rotation.values()[id];
    }

    public int rotateTo(Rotation rotation) {
        if (rotation == this) {
            return 0;
        }
        int id = rotation.ordinal();
        if (id == right) {
            return 1;
        }
        if (id == mid) {
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
