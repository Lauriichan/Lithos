package org.playuniverse.minecraft.core.lithos.custom.craft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.util.cooldown.CooldownQueue;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.spigot.language.MessageWrapper;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@EventInfo
public final class CraftingListener implements IListenerExtension {

    private static final Predicate<Entity> ITEM_PREDICATE = (entity) -> entity.getType() == EntityType.DROPPED_ITEM;

    private final CooldownQueue<UUID> cooldown = new CooldownQueue<>(25);
    private final CraftingHandler handler;

    public CraftingListener(ModuleWrapper<Lithos> wrapper) {
        this.handler = wrapper.getModule().getCraftingHandler();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!cooldown.enqueue(event.getPlayer().getUniqueId())) {
            return;
        }
        Location location = event.getClickedBlock().getLocation();
        CraftingStation station = handler.getStation(location);
        if (station == null) {
            return;
        }
        event.setCancelled(true);
        MessageWrapper<?> wrapper = MessageWrapper.of(event.getPlayer());
        Location center = location.clone().add(0.5, 1.5, 0.5);
        Collection<Entity> collection = location.getWorld().getNearbyEntities(center, 0.5, 0.5, 0.5, ITEM_PREDICATE);
        if (collection.isEmpty()) {
            wrapper.send("$prefix Bitte lege Items auf diesen Block um mit dieser Struktur craften zu können!");
            return;
        }
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (Entity entity : collection) {
            if (!(entity instanceof Item)) {
                continue;
            }
            Item item = (Item) entity;
            items.add(item);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setPersistent(true);
            stacks.add(item.getItemStack());
        }
        if (stacks.isEmpty()) {
            wrapper.send("$prefix Bitte lege Items auf diesen Block um mit dieser Struktur craften zu können!");
            return;
        }
        ItemStack[] ingredients = stacks.toArray(ItemStack[]::new);
        stacks.clear();
        ItemStack[] results = station.craft(ingredients);
        if (results == null) {
            for (Item item : items) {
                item.setPickupDelay(4);
                item.setPersistent(false);
            }
            items.clear();
            wrapper.send("$prefix Es gibt kein Rezept, das an dieser Struktur mit diesen Zutaten gecraftet werden kann!");
            return;
        }
        for (int index = 0; index < ingredients.length; index++) {
            if (ingredients[index].getAmount() == 0) {
                items.get(index).remove();
                continue;
            }
            Item item = items.get(index);
            item.setItemStack(ingredients[index]);
            item.setPickupDelay(4);
            item.setPersistent(false);
        }
        items.clear();
        World world = location.getWorld();
        for (ItemStack result : results) {
            world.dropItem(center, result);
        }
    }

}
