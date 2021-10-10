package org.playuniverse.minecraft.core.lithos.custom.craft;

import java.io.File;

import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.Files;

public final class CraftingHandler {

    private final StructureHandler structureHandler;
    private final File folder;

    public CraftingHandler(StructureHandler structureHandler, File folder) {
        this.folder = Files.createFolder(new File(folder, "crafting"));
        this.structureHandler = structureHandler;
    }

    public StructureHandler getStructureHandler() {
        return structureHandler;
    }

    public File getFolder() {
        return folder;
    }

}
