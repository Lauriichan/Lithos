package org.playuniverse.minecraft.core.lithos;

import org.playuniverse.minecraft.core.lithos.feature.FeatureHandler;
import org.playuniverse.minecraft.core.lithos.feature.event.IFeatureEvent;
import org.playuniverse.minecraft.mcs.spigot.module.SpigotCoreModule;

public final class Lithos extends SpigotCoreModule {

    private final FeatureHandler featureHandler = new FeatureHandler();

    @Override
    protected void onServerReady() {
        registerEvents();
    }

    /*
     * 
     */

    private void registerEvents() {
        getLogger().log("Registering feature events...");
        int[] result = IFeatureEvent.register(this);
        getLogger().log(String.format("Registered %s of %s feature events", result[0], result[1]));
    }

    /*
     * 
     */

    public FeatureHandler getFeatureHandler() {
        return featureHandler;
    }

}
