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

    public StructureHandler(final IOHandler ioHandler, final File folder) {
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

    public String[] getNames() {
        return pools.keySet().toArray(String[]::new);
    }

    public StructurePool get(final String name) {
        return pools.get(name.toLowerCase());
    }

    public boolean has(final String name) {
        return pools.containsKey(name);
    }

    /*
     * Creation
     */

    public boolean hasInfo(final UUID id) {
        return infos.containsKey(id);
    }

    public void prepare(final UUID id, final String name, final Rotation rotation, final Position first, final Position second) {
        infos.put(id, new StructureInfo(name.toLowerCase(), rotation, first, second));
    }

    public void create(final UUID id, final Location origin) {
        final StructureInfo info = infos.remove(id);
        final StructurePool pool = pools.computeIfAbsent(info.getName(), StructurePool::new);
        pool.saveStructure(origin, info.getRotation(), info.getFirst(), info.getSecond());
        final NbtCompound compound = ioHandler.serializeNbt(pool);
        try {
            NbtSerializer.COMPRESSED.toFile(new NbtNamedTag(info.getName(), compound), new File(folder, info.getName() + ".nbt"));
        } catch (final IOException e) {
            // Ignore for now
        }
    }

    /*
     * Load
     */

    public void load() {
        pools.clear();
        for (final File file : folder.listFiles()) {
            if (!file.getName().endsWith(".nbt")) {
                continue;
            }
            NbtCompound compound;
            try {
                final NbtTag tag = NbtDeserializer.COMPRESSED.fromFile(file).getTag();
                if (tag.getType() != NbtType.COMPOUND) {
                    continue;
                }
                compound = (NbtCompound) tag;
            } catch (final IOException e) {
                // Ignore for now
                continue;
            }
            final Object object = ioHandler.deserializeNbt(compound);
            if (!(object instanceof StructurePool)) {
                continue;
            }
            final StructurePool pool = (StructurePool) object;
            pools.put(pool.getName(), pool);
        }
    }

}
