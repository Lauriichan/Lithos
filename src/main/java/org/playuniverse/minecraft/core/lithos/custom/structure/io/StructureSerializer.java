package org.playuniverse.minecraft.core.lithos.custom.structure.io;

import static org.playuniverse.minecraft.core.lithos.util.BitHelper.merge11BitUnsigned;

import java.util.ArrayList;
import java.util.HashMap;

import org.playuniverse.minecraft.core.lithos.custom.structure.StructureBlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructurePool;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtIntArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtList;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtString;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "structure_pool", input = StructurePool.class, output = NbtCompound.class)
public final class StructureSerializer implements IDataExtension<StructurePool, NbtCompound> {

    @Override
    public NbtCompound convert(final StructurePool input) {
        if (input.getBaseRotation() == null) {
            return null;
        }
        final NbtCompound compound = new NbtCompound();
        compound.set("name", input.getName());
        compound.set("rotation", input.getBaseRotation().name());
        final ArrayList<String> list = new ArrayList<>();
        final HashMap<Position, StructureBlockData> map = input.getStructure(input.getBaseRotation()).getMap();
        final int[] blocks = new int[map.size()];
        int index = 0;
        for (final Position position : map.keySet()) {
            final String data = map.get(position).asBlockData();
            int id = list.indexOf(data);
            if (id == -1) {
                id = list.size();
                list.add(data);
            }
            blocks[index++] = position.toId() << 11 | merge11BitUnsigned(id);
        }
        final NbtList<NbtString> out = new NbtList<>();
        for (final String value : list) {
            out.add(new NbtString(value));
        }
        compound.set("ids", out);
        compound.set("blocks", new NbtIntArray(blocks));
        return compound;
    }

}
