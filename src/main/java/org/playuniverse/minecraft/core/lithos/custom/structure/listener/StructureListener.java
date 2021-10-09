package org.playuniverse.minecraft.core.lithos.custom.structure.listener;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.mcs.spigot.event.base.BukkitEventHandler;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
public class StructureListener implements IListenerExtension {

    private final StructureHandler handler;

    public StructureListener(ModuleWrapper<Lithos> wrapper) {
        this.handler = wrapper.getModule().getStructureHandler();
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
        handler.create(event.getPlayer().getUniqueId(), event.getClickedBlock().getLocation());
    }

}
