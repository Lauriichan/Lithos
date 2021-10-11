package org.playuniverse.minecraft.core.lithos;

import org.playuniverse.minecraft.core.lithos.custom.craft.CraftingHandler;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.core.lithos.economy.EconomyHandler;
import org.playuniverse.minecraft.core.lithos.feature.Feature;
import org.playuniverse.minecraft.core.lithos.feature.FeatureHandler;
import org.playuniverse.minecraft.core.lithos.feature.event.IFeatureEventExtension;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.UniCode;
import org.playuniverse.minecraft.mcs.spigot.language.placeholder.Placeholder;
import org.playuniverse.minecraft.mcs.spigot.language.placeholder.PlaceholderStore;
import org.playuniverse.minecraft.mcs.spigot.module.SpigotCoreModule;

public final class Lithos extends SpigotCoreModule {

    private final FeatureHandler featureHandler = new FeatureHandler();
    private final IOHandler ioHandler = new IOHandler();

    private StructureHandler structureHandler;
    private CraftingHandler craftingHandler;
    private EconomyHandler economyHandler;

    @Override
    protected void onLoad() {
        structureHandler = new StructureHandler(ioHandler, getDataLocation());
        craftingHandler = new CraftingHandler(ioHandler, structureHandler, getDataLocation());
        economyHandler = new EconomyHandler(ioHandler, getDataLocation());

        final PlaceholderStore store = getDefaultPlaceholders();
        store.setPlaceholder(Placeholder.of("prefix", "&cLithos &8" + UniCode.ARROWS_RIGHT + "&7"));
    }
    
    @Override
    protected void onServerReady() {
        registerExtensions();
        loadData();
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
        getLogger().log("Registering features...");
        result = Feature.register(this);
        getLogger().log(String.format("Registered %s of %s features", result[0], result[1]));

        getLogger().log("Registering feature events...");
        result = IFeatureEventExtension.register(this);
        getLogger().log(String.format("Registered %s of %s feature events", result[0], result[1]));

        getLogger().log("Registering io converter...");
        result = IDataExtension.register(this);
        getLogger().log(String.format("Registered %s of %s io converter", result[0], result[1]));
    }

    private void loadData() {
        structureHandler.load();
        craftingHandler.load();
        economyHandler.load();
    }
    
    /*
     * On Unload
     */
    
    private void saveData() {
        economyHandler.save();
    }

    /*
     * Getter
     */

    public FeatureHandler getFeatureHandler() {
        return featureHandler;
    }

    public StructureHandler getStructureHandler() {
        return structureHandler;
    }
    
    public CraftingHandler getCraftingHandler() {
        return craftingHandler;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

    public IOHandler getIOHandler() {
        return ioHandler;
    }

}
