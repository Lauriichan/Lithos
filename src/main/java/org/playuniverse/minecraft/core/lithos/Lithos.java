package org.playuniverse.minecraft.core.lithos;

import java.util.ArrayList;

import org.playuniverse.minecraft.core.lithos.custom.craft.CraftingHandler;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.logging.LogTypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.UniCode;
import org.playuniverse.minecraft.mcs.spigot.language.placeholder.Placeholder;
import org.playuniverse.minecraft.mcs.spigot.language.placeholder.PlaceholderStore;
import org.playuniverse.minecraft.mcs.spigot.module.SpigotCoreModule;

public final class Lithos extends SpigotCoreModule {

    private final ArrayList<Runnable> shutdown = new ArrayList<>();

    private final IOHandler ioHandler = new IOHandler();

    private StructureHandler structureHandler;
    private CraftingHandler craftingHandler;

    @Override
    protected void onLoad() {
        structureHandler = new StructureHandler(ioHandler, getDataLocation());
        craftingHandler = new CraftingHandler(ioHandler, structureHandler, getDataLocation());

        final PlaceholderStore store = getDefaultPlaceholders();
        store.setPlaceholder(Placeholder.of("prefix", "&cLithos &8" + UniCode.ARROWS_RIGHT + "&7"));
    }

    @Override
    protected void onServerReady() {
        registerExtensions();
        loadData();
    }

    @Override
    protected void onStop() {
        Runnable[] actions = shutdown.toArray(Runnable[]::new);
        shutdown.clear();
        for (Runnable action : actions) {
            try {
                action.run();
            } catch (Exception exp) {
                getLogger().log(LogTypeId.WARNING, "Error while shutting down");
                getLogger().log(LogTypeId.WARNING, exp);
            }
        }
    }

    @Override
    protected void onUnload() {
        saveData();
    }

    /*
     * On Load
     */

    private void registerExtensions() {
        int[] result;
        getLogger().log("Registering io converter...");
        result = IDataExtension.register(this);
        getLogger().log(String.format("Registered %s of %s io converter", result[0], result[1]));
    }

    private void loadData() {
        structureHandler.load();
        craftingHandler.load();
    }

    /*
     * On Unload
     */

    private void saveData() {}

    /*
     * Shutdown helper
     */

    public void addShutdown(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        shutdown.add(runnable);
    }

    /*
     * Getter
     */

    public StructureHandler getStructureHandler() {
        return structureHandler;
    }

    public CraftingHandler getCraftingHandler() {
        return craftingHandler;
    }

    public IOHandler getIOHandler() {
        return ioHandler;
    }

}
