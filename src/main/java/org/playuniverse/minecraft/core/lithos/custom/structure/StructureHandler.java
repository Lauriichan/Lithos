package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class StructureHandler {

    private final HashMap<String, StructurePool> pools = new HashMap<>();
    private final HashMap<UUID, StructureInfo> infos = new HashMap<>();

    private final File folder;

    public StructureHandler(File folder) {
        this.folder = new File(folder, "structures");
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
    }

}
