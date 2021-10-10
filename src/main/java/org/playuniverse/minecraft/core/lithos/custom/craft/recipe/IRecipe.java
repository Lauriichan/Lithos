package org.playuniverse.minecraft.core.lithos.custom.craft.recipe;

import org.bukkit.inventory.ItemStack;

public interface IRecipe {
    
    ItemStack[] craft(ItemStack[] input);

}
