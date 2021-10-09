package org.playuniverse.minecraft.core.lithos.custom.structure;

import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class StructureInfo {

    private final Position first, second;
    private final Rotation rotation;
    private final String name;

    public StructureInfo(String name, Rotation rotation, Position first, Position second) {
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
