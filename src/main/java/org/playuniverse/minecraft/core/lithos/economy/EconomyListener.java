package org.playuniverse.minecraft.core.lithos.economy;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension.Target;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;

@Extension
@EventInfo(target = Target.BUKKIT)
public final class EconomyListener implements IListenerExtension {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (!shouldBeBlocked(event.getInventory().getContents())) {
            return;
        }
        event.getInventory().setResult(null);
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        if (!shouldBeBlocked(event.getInventory().getContents())) {
            return;
        }
        event.setResult(null);
    }

    public boolean shouldBeBlocked(ItemStack[] contents) {
        for (ItemStack stack : contents) {
            if (stack == null || stack.getType() != Material.AIR) {
                continue;
            }
            for (ItemStack value : EconomyHandler.VALUES) {
                if (stack.isSimilar(value)) {
                    return true;
                }
            }
        }
        return false;
    }

}
