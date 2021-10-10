package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.util.HashMap;

import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class Structure {

    private final HashMap<Position, StructureBlockData> map = new HashMap<>();
    private final Rotation rotation;

    private final StructurePool parent;

    public Structure(StructurePool parent, Rotation rotation) {
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

    Structure create() {
        if (!map.isEmpty()) {
            return this; // Already created
        }
        HashMap<Position, StructureBlockData> base = parent.getStructure(parent.getBaseRotation()).getMap();
        int amount = parent.getBaseRotation().rotateTo(rotation);
        for (Position position : base.keySet()) {
            map.put(position.rotate(amount), base.get(position).clone().rotate(amount));
        }
        return this;
    }

}
