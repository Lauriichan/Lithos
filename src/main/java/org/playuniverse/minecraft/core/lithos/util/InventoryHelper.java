package org.playuniverse.minecraft.core.lithos.util;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryHelper {

    private InventoryHelper() {}

    public static void add(Location location, Inventory inventory, ItemStack sample, long amount) {
        int max = sample.getMaxStackSize();
        while (amount != 0) {
            ItemStack out = sample.clone();
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

    private static void add(Location location, Inventory inventory, ItemStack stack) {
        HashMap<Integer, ItemStack> map = inventory.addItem(stack);
        if (map.isEmpty()) {
            return;
        }
        location.getWorld().dropItem(location, map.get(0));
    }

    public static void remove(Inventory inventory, ItemStack sample, long amount) {
        int size = inventory.getSize();
        for (int index = 0; index < size; index++) {
            ItemStack stack = inventory.getItem(index);
            if (stack == null || stack.getType() == Material.AIR || !sample.isSimilar(stack)) {
                continue;
            }
            long rest = amount - stack.getAmount();
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
