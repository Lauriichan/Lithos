package org.playuniverse.minecraft.core.lithos.util;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryHelper {

    private InventoryHelper() {}

    public static void add(final Location location, final Inventory inventory, final ItemStack sample, long amount) {
        final int max = sample.getMaxStackSize();
        while (amount != 0) {
            final ItemStack out = sample.clone();
            if (amount > max) {
                out.setAmount(max);
                add(location, inventory, out);
                amount -= max;
                continue;
            }
            out.setAmount((int) amount);
            add(location, inventory, out);
            break;
        }
    }

    private static void add(final Location location, final Inventory inventory, final ItemStack stack) {
        final HashMap<Integer, ItemStack> map = inventory.addItem(stack);
        if (map.isEmpty()) {
            return;
        }
        location.getWorld().dropItem(location, map.get(0));
    }

    public static void remove(final Inventory inventory, final ItemStack sample, final long amount) {
        final int size = inventory.getSize();
        for (int index = 0; index < size; index++) {
            final ItemStack stack = inventory.getItem(index);
            if (stack == null || stack.getType() == Material.AIR || !sample.isSimilar(stack)) {
                continue;
            }
            final long rest = amount - stack.getAmount();
            if (rest > 0) {
                inventory.setItem(index, null);
                continue;
            }
            if (rest == 0) {
                inventory.setItem(index, null);
                return;
            }
            stack.setAmount((int) (stack.getAmount() - rest));
            return;
        }
    }

}
