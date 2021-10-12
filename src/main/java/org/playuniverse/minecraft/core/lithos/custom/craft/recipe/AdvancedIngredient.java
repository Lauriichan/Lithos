package org.playuniverse.minecraft.core.lithos.custom.craft.recipe;

import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.util.bukkit.MetaCheck;

public class AdvancedIngredient implements IIngredient {

    private final ItemStack item;
    private final int amount;

    private final MetaCheck[] checks;

    public AdvancedIngredient(ItemStack item, int amount, MetaCheck[] checks) {
        this.checks = checks;
        this.item = item;
        this.amount = amount;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public MetaCheck[] getChecks() {
        return checks;
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack.getType() != item.getType() || stack.hasItemMeta() == item.hasItemMeta()) {
            return false;
        }
        if ((item.hasItemMeta() && checks.length != 0) && !MetaCheck.isSimilar(item.getItemMeta(), stack.getItemMeta(), checks)) {
            return false;
        }
        return stack.getAmount() == amount;
    }

    @Override
    public void modify(ItemStack stack) {
        stack.setAmount(stack.getAmount() - amount);
    }

}
