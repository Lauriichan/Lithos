package org.playuniverse.minecraft.core.lithos.custom.craft.animation;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.playuniverse.minecraft.core.lithos.custom.craft.CraftProcess;

public final class BasicCraftAnimation implements ICraftAnimation {

    @Override
    public void finish(CraftProcess process) {
        ItemStack[] ingredients = process.getIngredients();
        ArrayList<Item> items = process.getItems();
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
        World world = process.getLocation().getWorld();
        for (ItemStack result : process.getResults()) {
            world.dropItem(process.getCenter(), result).setVelocity(new Vector(0, 0, 0));
        }
    }

    @Override
    public void step(CraftProcess process) {
        Location location = process.getCenter();
        location.getWorld().spawnParticle(Particle.CRIT, location, 1, 0.15, 0.15, 0.15, 0);
    }

}
