package org.playuniverse.minecraft.core.lithos.custom.craft.recipe;

import org.bukkit.inventory.ItemStack;

public interface IIngredient {
    
    boolean isSimilar(ItemStack stack);
    
    void modify(ItemStack stack);

}
