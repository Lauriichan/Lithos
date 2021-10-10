package org.playuniverse.minecraft.core.lithos.custom.structure;

import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class StructureInfo {

    private final Position first, second;
    private final Rotation rotation;
    private final String name;

    public StructureInfo(final String name, final Rotation rotation, final Position first, final Position second) {
        this.first = first;
        this.second = second;
        this.rotation = rotation;
        this.name = name;
    }

    public Position getFirst() {
        return first;
    }

    public Position getSecond() {
        return second;
    }

    public String getName() {
        return name;
    }

    public Rotation getRotation() {
        return rotation;
    }

}
