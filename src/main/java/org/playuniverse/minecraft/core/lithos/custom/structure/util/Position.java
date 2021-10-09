package org.playuniverse.minecraft.core.lithos.custom.structure.util;

import org.bukkit.util.Vector;
import org.playuniverse.minecraft.mcs.spigot.utils.java.math.Axis;
import org.playuniverse.minecraft.mcs.spigot.utils.java.math.Quaternion;

import static org.playuniverse.minecraft.core.lithos.util.BitHelper.*;

public final class Position {

    private final int x, y, z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(int id) {
        this.x = unmerge7Bit(id) >> 14;
        this.y = unmerge7Bit(id) >> 7;
        this.z = unmerge7Bit(id) >> 0;
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

    public Position add(int x, int y, int z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public Position min(Position other) {
        return new Position(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z));
    }

    public Position max(Position other) {
        return new Position(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z));
    }

    public Position rotate(int amount) {
        Vector vector = Quaternion.of(x, y, z).rotate(Axis.Y, amount * 90f).getAxis(Axis.Y);
        return new Position(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public int toId() {
        return merge7Bit(x) << 14 | merge7Bit(y) << 7 | merge7Bit(z) << 0;
    }

}
