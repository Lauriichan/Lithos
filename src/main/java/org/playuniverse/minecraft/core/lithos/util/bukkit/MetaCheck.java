package org.playuniverse.minecraft.core.lithos.util.bukkit;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.bukkit.inventory.meta.ItemMeta;

public enum MetaCheck {

    DISPLAY_NAME(ItemMeta::hasDisplayName, MetaChecks::checkDisplayName),
    LOCALIZED_NAME(ItemMeta::hasLocalizedName, MetaChecks::checkLocalizedName),
    LORE(ItemMeta::hasLore, MetaChecks::checkLore),
    MODEL(ItemMeta::hasCustomModelData, MetaChecks::checkModelId),
    ENCHANTMENTS(ItemMeta::hasEnchants, MetaChecks::checkEnchants),
    BLOCK_DATA(MetaChecks::hasBlockData, MetaChecks::checkBlockData),
    REPAIR_COST(MetaChecks::hasRepairCost, MetaChecks::checkRepairCost),
    ATTRIBUTE_MODIFIERS(ItemMeta::hasAttributeModifiers, MetaChecks::checkAttributeModifiers),
    DURABILITY(MetaChecks::hasDamage, MetaChecks::checkDamage),
    FLAGS(MetaChecks::hasFlags, MetaChecks::checkFlags),
    CUSTOM_TAGS(MetaChecks::hasPersistentData, MetaChecks::checkPersistentData),
    UNCOMMON(MetaChecks::hasUncommon, MetaChecks::checkUncommon); // TODO: Maybe check everything manually

    private final Predicate<ItemMeta> has;
    private final BiPredicate<ItemMeta, ItemMeta> check;

    private MetaCheck(Predicate<ItemMeta> has, BiPredicate<ItemMeta, ItemMeta> check) {
        this.has = has;
        this.check = check;
    }

    public boolean has(ItemMeta meta) {
        return has != null ? has.test(meta) : true;
    }

    public boolean check(ItemMeta meta1, ItemMeta meta2) {
        return !(has(meta1) && has(meta2)) ? false : (check == null ? true : check.test(meta1, meta2));
    }

    public static MetaCheck fromString(String name) {
        try {
            return MetaCheck.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    public static boolean isSimilar(ItemMeta first, ItemMeta second, MetaCheck... checks) {
        for (MetaCheck check : checks) {
            if (!check.check(first, second)) {
                return false;
            }
        }
        return true;
    }

}
