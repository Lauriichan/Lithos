package org.playuniverse.minecraft.core.lithos.custom.craft;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.custom.craft.animation.ICraftAnimation;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.IRecipe;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructurePool;

public final class CraftingStation {

    private final String name;
    private final ArrayList<IRecipe> recipes = new ArrayList<>();
    private final ArrayList<ICraftAnimation> animations = new ArrayList<>();

    private final int time;

    public CraftingStation(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }
    
    public int getTime() {
        return time;
    }

    public void add(IRecipe recipe) {
        if (recipes.contains(recipe)) {
            return;
        }
        recipes.add(recipe);
    }

    public void remove(IRecipe recipe) {
        recipes.remove(recipe);
    }

    public void add(ICraftAnimation animation) {
        if (animations.contains(animation)) {
            return;
        }
        animations.add(animation);
    }

    public void remove(ICraftAnimation animation) {
        animations.remove(animation);
    }

    public IRecipe[] getRecipes() {
        return recipes.toArray(IRecipe[]::new);
    }

    public ICraftAnimation[] getAnimations() {
        return animations.toArray(ICraftAnimation[]::new);
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
