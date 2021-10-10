package org.playuniverse.minecraft.core.lithos.custom.structure.io;

import java.util.HashMap;

import org.playuniverse.minecraft.core.lithos.custom.structure.StructureBlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructurePool;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtString;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtType;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtList;

import com.syntaxphoenix.avinity.module.extension.Extension;

import static org.playuniverse.minecraft.core.lithos.util.BitHelper.*;

@Extension
@TypeId(name = "structure_pool", input = NbtCompound.class, output = StructurePool.class)
public final class StructureDeserializer implements IDataExtension<NbtCompound, StructurePool> {

    @SuppressWarnings("unchecked")
    @Override
    public StructurePool convert(NbtCompound input) {
        if (!input.hasKey("name", NbtType.STRING) || !input.hasKey("rotation", NbtType.STRING) || !input.hasKey("ids", NbtType.LIST)
            || !input.hasKey("blocks", NbtType.INT_ARRAY)) {
            return null;
        }
        StructurePool pool = new StructurePool(input.getString("name"));
        String rotationRaw = input.getString("rotation");
        Rotation rotation = Rotation.fromString(rotationRaw);
        if (!rotation.name().equalsIgnoreCase(rotationRaw)) {
            return null; // Invalid
        }
        NbtList<?> idsRaw = input.getTagList("ids");
        if (idsRaw.getElementType() != NbtType.STRING) {
            return null;
        }
        NbtList<NbtString> ids = (NbtList<NbtString>) idsRaw;
        int[] blocks = input.getIntArray("blocks");
        pool.loadStructure(rotation, structure -> {
            HashMap<Position, StructureBlockData> map = structure.getMap();
            for (int index = 0; index < blocks.length; index++) {
                int block = blocks[index];
                Position position = new Position(block >> 11);
                map.put(position, StructureBlockData.of(ids.get(unmerge11BitUnsigned(block)).getValue()));
            }
        });
        return pool;
    }

}
