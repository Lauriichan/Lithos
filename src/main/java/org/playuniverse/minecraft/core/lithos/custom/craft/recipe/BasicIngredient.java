package org.playuniverse.minecraft.core.lithos.custom.craft.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class BasicIngredient implements IIngredient {

    private final Material material;
    private final int amount;

    public BasicIngredient(Material material, int amount) {
        this.material = material;
        this.amount = amount;
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
        return stack.getAmount() == amount;
    }

    @Override
    public void modify(ItemStack stack) {
        stack.setAmount(stack.getAmount() - amount);
    }

}
