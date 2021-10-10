package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class Structure {

    private final HashMap<Position, StructureBlockData> map = new HashMap<>();
    private final Rotation rotation;

    private final StructurePool parent;

    public Structure(final StructurePool parent, final Rotation rotation) {
        this.parent = parent;
        this.rotation = rotation;
    }

    public HashMap<Position, StructureBlockData> getMap() {
        return map;
    }

    public StructurePool getParent() {
        return parent;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public boolean isBuild(final Location location) {
        final World world = location.getWorld();
        final Position origin = new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        final Position[] positions = map.keySet().toArray(Position[]::new);
        final HashMap<String, BlockData> cache = new HashMap<>();
        for (final Position position : positions) {
            final StructureBlockData data = map.get(position);
            if (data == null) {
                continue;
            }
            final BlockData bukkitData = cache.computeIfAbsent(data.asBlockData(), Bukkit::createBlockData);
            final BlockData blockData = world.getBlockAt(position.getX(origin), position.getY(origin), position.getZ(origin))
                .getBlockData();
            if (!bukkitData.matches(blockData)) {
                cache.clear();
                return false;
            }
        }
        cache.clear();
        return true;
    }

    Structure create() {
        if (!map.isEmpty()) {
            return this; // Already created
        }
        final HashMap<Position, StructureBlockData> base = parent.getStructure(parent.getBaseRotation()).getMap();
        final int amount = parent.getBaseRotation().rotateTo(rotation);
        for (final Position position : base.keySet()) {
            map.put(position.rotate(amount), base.get(position).clone().rotate(amount));
        }
        return this;
    }

}
