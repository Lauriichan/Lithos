package org.playuniverse.minecraft.core.lithos.listener;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.event.ModuleDisableEvent;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@EventInfo(bukkit = false)
public final class ModuleListener implements IListenerExtension {

    private final Lithos lithos;

    public ModuleListener(final ModuleWrapper<Lithos> wrapper) {
        this.lithos = wrapper.getModule();
    }

    @EventHandler
    public void onModuleDisable(final ModuleDisableEvent event) {

        final ModuleWrapper<?> wrapper = event.getWrapper();

        lithos.getFeatureHandler().unregister(wrapper);
        lithos.getIOHandler().unregister(wrapper);

    }

}
