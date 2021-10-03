package org.playuniverse.minecraft.core.lithos;

import java.io.File;

import org.pf4j.PluginWrapper;
import org.playuniverse.minecraft.core.lithos.extension.ICommandExtension;
import org.playuniverse.minecraft.mcs.spigot.plugin.SpigotCorePlugin;

public final class Lithos extends SpigotCorePlugin {

    public Lithos(PluginWrapper wrapper, File dataLocation) {
        super(wrapper, dataLocation);
    }

    @Override
    protected void onStart() {
        ICommandExtension.register(this);
    }

    @Override
    protected void onServerReady() {

    }

    @Override
    protected void onStop() {

    }

}
