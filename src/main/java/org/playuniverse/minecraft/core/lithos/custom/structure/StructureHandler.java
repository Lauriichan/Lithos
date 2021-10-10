package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtNamedTag;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtTag;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtType;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtDeserializer;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtSerializer;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.Files;

public final class StructureHandler {

    private final HashMap<String, StructurePool> pools = new HashMap<>();
    private final HashMap<UUID, StructureInfo> infos = new HashMap<>();

    private final File folder;
    private final IOHandler ioHandler;

    public StructureHandler(IOHandler ioHandler, File folder) {
        this.ioHandler = ioHandler;
        this.folder = Files.createFolder(new File(folder, "structures"));
    }

    /*
     * Getter
     */

    public File getFolder() {
        return folder;
    }

    /*
     * Structure Pools
     */

    public StructurePool get(String name) {
        return pools.get(name.toLowerCase());
    }

    public boolean has(String name) {
        return pools.containsKey(name);
    }

    /*
     * Creation
     */

    public boolean hasInfo(UUID id) {
        return infos.containsKey(id);
    }

    public void prepare(UUID id, String name, Rotation rotation, Position first, Position second) {
        infos.put(id, new StructureInfo(name.toLowerCase(), rotation, first, second));
    }

    public void create(UUID id, Location origin) {
        StructureInfo info = infos.remove(id);
        StructurePool pool = pools.computeIfAbsent(info.getName(), name -> new StructurePool(name));
        pool.saveStructure(origin, info.getRotation(), info.getFirst(), info.getSecond());
        NbtCompound compound = ioHandler.serialize(pool);
        try {
            NbtSerializer.COMPRESSED.toFile(new NbtNamedTag(info.getName(), compound), new File(folder, info.getName() + ".nbt"));
        } catch (IOException e) {
            // Ignore for now
        }
    }

    /*
     * Load
     */

    public void load() {
        for (File file : folder.listFiles()) {
            if (!file.getName().endsWith(".nbt")) {
                continue;
            }
            NbtCompound compound;
            try {
                NbtTag tag = NbtDeserializer.COMPRESSED.fromFile(file).getTag();
                if (tag.getType() != NbtType.COMPOUND) {
                    continue;
                }
                compound = (NbtCompound) tag;
            } catch (IOException e) {
                // Ignore for now
                continue;
            }
            Object object = ioHandler.deserialize(compound);
            if (!(object instanceof StructurePool)) {
                continue;
            }
            StructurePool pool = (StructurePool) object;
            pools.put(pool.getName(), pool);
        }
    }

}
