package org.playuniverse.minecraft.core.lithos.listener;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.ModuleWrapper;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.event.ModuleDisableEvent;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension.Target;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;

@Extension
@EventInfo(target = Target.SYNTAX)
public final class ModuleListener implements IListenerExtension {

    private final Lithos lithos;

    public ModuleListener(final ModuleWrapper<Lithos> wrapper) {
        this.lithos = wrapper.getModule();
    }

    @EventHandler
    public void onModuleDisable(final ModuleDisableEvent event) {

        final ModuleWrapper<?> wrapper = event.getWrapper();
        
        lithos.getIOHandler().unregister(wrapper);

    }

}
