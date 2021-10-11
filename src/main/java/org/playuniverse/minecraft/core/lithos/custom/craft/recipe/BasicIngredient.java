package org.playuniverse.minecraft.core.lithos.custom.craft.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public final class BasicIngredient implements IIngredient {

    private final boolean ignoreDurability;
    private final Material material;
    private final int amount;

    public BasicIngredient(Material material, int amount, boolean ignoreDurability) {
        this.ignoreDurability = material.getMaxDurability() <= 0 || ignoreDurability;
        this.material = material;
        this.amount = amount;
    }

    public boolean isDurabilityIgnored() {
        return ignoreDurability;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack.getType() != material) {
            return false;
        }
        if (!ignoreDurability && stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta instanceof Damageable && ((Damageable) meta).hasDamage()) {
                return false;
            }
        }
        return stack.getAmount() == amount;
    }

    @Override
    public void modify(ItemStack stack) {
        stack.setAmount(stack.getAmount() - amount);
    }

    @Override
    public String toString() {
        return "{material:" + material.name() + ",amount:" + amount + "}";
    }

}
