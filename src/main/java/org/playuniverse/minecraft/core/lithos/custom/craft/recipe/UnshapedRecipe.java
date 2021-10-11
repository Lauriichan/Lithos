package org.playuniverse.minecraft.core.lithos.custom.craft.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

public final class UnshapedRecipe implements IRecipe {

    private final ArrayList<IIngredient> ingredients = new ArrayList<>();
    private final ItemStack result;

    public UnshapedRecipe(ItemStack result) {
        this.result = result;
    }

    public ArrayList<IIngredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result.clone();
    }

    @Override
    public ItemStack[] craft(ItemStack[] input) {
        if (ingredients.size() != input.length) {
            return null;
        }
        HashMap<IIngredient, ItemStack> map = new HashMap<>();
        for (IIngredient ingredient : ingredients) {
            for (ItemStack item : input) {
                if (map.containsValue(item)) {
                    continue;
                }
                if (!ingredient.isSimilar(item)) {
                    continue;
                }
                map.put(ingredient, item);
                break;
            }
            if (!map.containsKey(ingredient)) {
                return null;
            }
        }
        for (Entry<IIngredient, ItemStack> entry : map.entrySet()) {
            entry.getKey().modify(entry.getValue());
        }
        return new ItemStack[] {
            getResult()
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[result:");
        builder.append(result.toString());
        builder.append(",ingredients:[");
        for (IIngredient ingredient : ingredients) {
            builder.append(ingredient.toString());
            builder.append(',');
        }
        return ingredients.isEmpty() ? builder.append("]]").toString() : builder.substring(0, builder.length() - 1) + "]]";
    }

}
