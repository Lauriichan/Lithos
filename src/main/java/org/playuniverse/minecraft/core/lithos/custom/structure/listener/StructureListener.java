package org.playuniverse.minecraft.core.lithos.custom.structure.listener;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.logging.LogTypeId;
import org.playuniverse.minecraft.mcs.spigot.event.base.BukkitEventHandler;
import org.playuniverse.minecraft.mcs.spigot.language.MessageWrapper;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@EventInfo
public class StructureListener implements IListenerExtension {

    private final StructureHandler handler;
    private final Lithos module;

    public StructureListener(ModuleWrapper<Lithos> wrapper) {
        this.module = wrapper.getModule();
        this.handler = module.getStructureHandler();
    }

    @BukkitEventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getMaterial() != Material.STICK) {
            return;
        }
        if (!handler.hasInfo(event.getPlayer().getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        MessageWrapper<?> message = MessageWrapper.of(event.getPlayer(), module);
        try {
            handler.create(event.getPlayer().getUniqueId(), event.getClickedBlock().getLocation());
        } catch (Exception exp) { // Normally nothing should happen but in case of smth
            message.send("$prefix Something went wrong while saving your structure!");
            module.getLogger().log(LogTypeId.WARNING, exp);
            return;
        }
        message.send("$prefix Successfully saved structure!");
    }

}
