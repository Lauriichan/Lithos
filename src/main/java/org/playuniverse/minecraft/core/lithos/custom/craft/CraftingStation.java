package org.playuniverse.minecraft.core.lithos.custom.craft;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.IRecipe;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructurePool;

public final class CraftingStation {

    private final String name;
    private final ArrayList<IRecipe> recipes = new ArrayList<>();

    public CraftingStation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void add(IRecipe recipe) {
        if(recipes.contains(recipe)) {
            return;
        }
        recipes.add(recipe);
    }
    
    public void remove(IRecipe recipe) {
        recipes.remove(recipe);
    }
    
    public IRecipe[] getRecipes() {
        return recipes.toArray(IRecipe[]::new);
    }

    public boolean isBuild(CraftingHandler handler, Location location) {
        StructurePool pool = handler.getStructureHandler().get(name);
        return pool == null ? false : pool.isBuild(location);
    }

    public ItemStack[] craft(ItemStack[] input) {
        if (recipes.isEmpty()) {
            return null;
        }
        ItemStack[] output = null;
        for (int index = 0; index < recipes.size(); index++) {
            output = recipes.get(index).craft(input);
            if (output != null) {
                return output;
            }
        }
        return null;
    }

}
