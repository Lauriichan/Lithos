package org.playuniverse.minecraft.core.lithos.custom.craft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonValue;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.ValueType;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.io.JsonParser;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.Files;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.Streams;

public final class CraftingHandler {

    private final JsonParser parser = new JsonParser();
    
    private final StructureHandler structureHandler;
    private final IOHandler ioHandler;
    private final File folder;

    private final HashMap<String, CraftingStation> stations = new HashMap<>();

    public CraftingHandler(IOHandler ioHandler, StructureHandler structureHandler, File folder) {
        this.folder = new File(folder, "crafting");
        this.ioHandler = ioHandler;
        this.structureHandler = structureHandler;
    }
    
    public CraftingStation getStation(Location location) {
        for(CraftingStation station : stations.values()) {
            if(station.isBuild(this, location)) {
                return station;
            }
        }
        return null;
    }

    public StructureHandler getStructureHandler() {
        return structureHandler;
    }

    public File getFolder() {
        return folder;
    }

    public void load() {
        stations.clear();
        Files.createFolder(folder);
        for (File file : folder.listFiles()) {
            if (!file.getName().endsWith(".json")) {
                continue;
            }
            JsonObject jsonObject;
            try {
                JsonValue<?> value = parser.fromString(Streams.toString(new FileInputStream(file)));
                if (value == null || !value.hasType(ValueType.OBJECT)) {
                    continue;
                }
                jsonObject = (JsonObject) value;
            } catch (IOException e) {
                continue;
            }
            Object object = ioHandler.deserializeJson(jsonObject);
            if (!(object instanceof CraftingStation)) {
                continue;
            }
            CraftingStation station = (CraftingStation) object;
            if (stations.containsKey(station.getName())) {
                continue;
            }
            stations.put(station.getName(), station);
        }
    }

}
