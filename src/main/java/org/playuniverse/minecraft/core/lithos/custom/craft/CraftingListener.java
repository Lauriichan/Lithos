package org.playuniverse.minecraft.core.lithos.custom.craft;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@EventInfo
public final class CraftingListener implements IListenerExtension {
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        /*
         * TODO(Lauriichan): 
         * 
         * Find out how to actually detect the items (see in previous projects)
         * And how to trigger the crafting mechanism
         */
    }

}
