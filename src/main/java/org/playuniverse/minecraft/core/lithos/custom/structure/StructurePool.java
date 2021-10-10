package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Consumer;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class StructurePool {

    private final EnumMap<Rotation, Structure> structures = new EnumMap<>(Rotation.class);
    private final String name;

    private Rotation baseRotation;

    public StructurePool(final String name) {
        this.name = name;
    }

    public Rotation getBaseRotation() {
        return baseRotation;
    }

    public String getName() {
        return name;
    }

    public boolean isBuild(final Location location) {
        for (final Rotation rotation : Rotation.values()) {
            if (!isBuild(location, rotation)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public boolean isBuild(final Location location, final Rotation rotation) {
        return getStructure(rotation).isBuild(location);
    }

    public Structure getStructure(final Rotation rotation) {
        return structures.computeIfAbsent(rotation, input -> new Structure(this, rotation).create());
    }

    public StructurePool loadStructure(final Rotation rotation, final Consumer<Structure> loader) {
        Objects.requireNonNull(rotation, "Rotation can't be null!");
        Objects.requireNonNull(loader, "Consumer<Structure> loader can't be null!");
        final Structure structure = new Structure(this, rotation);
        loader.accept(structure);
        structures.clear();
        this.baseRotation = rotation;
        structures.put(rotation, structure);
        return this;
    }

    public StructurePool saveStructure(final Location bukkitOrigin, final Rotation rotation, final Position first, final Position second) {
        Objects.requireNonNull(rotation, "Rotation can't be null!");
        Objects.requireNonNull(first, "First position can't be null!");
        Objects.requireNonNull(second, "Second position can't be null!");
        Objects.requireNonNull(bukkitOrigin, "Location origin can't be null!");
        final World world = Objects.requireNonNull(bukkitOrigin.getWorld(), "Origin world can't be null!");
        final Structure structure = new Structure(this, rotation);
        final Position origin = new Position(bukkitOrigin.getBlockX(), bukkitOrigin.getBlockY(), bukkitOrigin.getBlockZ());
        final Position min = first.min(second);
        final Position max = first.max(second);
        final HashMap<Position, StructureBlockData> map = structure.getMap();
        for (int x = min.getX(); x < max.getX(); x++) {
            for (int z = min.getZ(); z < max.getZ(); z++) {
                for (int y = min.getY(); y < max.getY(); y++) {
                    final BlockData data = world.getBlockAt(x, y, z).getBlockData();
                    if (data == null) {
                        continue;
                    }
                    map.put(origin.subtract(x, y, z).inverse(), StructureBlockData.of(data));
                }
            }
        }
        structures.clear();
        this.baseRotation = rotation;
        structures.put(rotation, structure);
        return this;
    }

}
