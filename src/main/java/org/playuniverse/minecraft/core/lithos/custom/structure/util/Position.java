package org.playuniverse.minecraft.core.lithos.custom.structure.util;

import static org.playuniverse.minecraft.core.lithos.util.BitHelper.merge7Bit;
import static org.playuniverse.minecraft.core.lithos.util.BitHelper.unmerge7Bit;

public final class Position {

    private final int x, y, z;

    public Position(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(final int id) {
        this.x = unmerge7Bit(id >> 14);
        this.y = unmerge7Bit(id >> 7);
        this.z = unmerge7Bit(id >> 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getX(final Position other) {
        return x + other.x;
    }

    public int getY(final Position other) {
        return y + other.y;
    }

    public int getZ(final Position other) {
        return z + other.z;
    }

    public Position add(final int x, final int y, final int z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public Position subtract(final int x, final int y, final int z) {
        return new Position(this.x - x, this.y - y, this.z - z);
    }

    public Position min(final Position other) {
        return new Position(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z));
    }

    public Position max(final Position other) {
        return new Position(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z));
    }

    public Position rotate(int amount) {
        amount = amount % 4;
        int x = this.x;
        int z = this.z;
        int tmp = 0;
        while (amount-- != 0) {
            tmp = -z;
            z = x;
            x = tmp;
        }
        return new Position(x, y, z);
    }

    public Position inverse() {
        return new Position(-x, -y, -z);
    }

    public int toId() {
        return merge7Bit(x) << 14 | merge7Bit(y) << 7 | merge7Bit(z) << 0;
    }

    @Override
    public String toString() {
        return String.format("[%s %s %s]", x, y, z);
    }

}
