package org.playuniverse.minecraft.core.lithos.util.bukkit;

import java.util.Objects;

import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.playuniverse.minecraft.vcompat.reflection.reflect.ClassLookupProvider;
import org.playuniverse.minecraft.vcompat.reflection.reflect.handle.ClassLookup;

final class MetaChecks {

    static final ClassLookup META = ClassLookupProvider.DEFAULT.createCBLookup("craftMeta", "inventory.CraftMetaItem");

    static {
        META.searchMethod("compare", "notUncommon", META.getOwner());
        META.searchField("blockData", "blockData");
    }

    private MetaChecks() {}

    static boolean checkDisplayName(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getDisplayName().equals(meta2.getDisplayName());
    }
    
    static boolean checkLocalizedName(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getLocalizedName().equals(meta2.getLocalizedName());
    }

    static boolean checkLore(ItemMeta meta1, ItemMeta meta2) {
        return Objects.equals(meta1.getLore(), meta2.getLore());
    }

    static boolean checkModelId(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getCustomModelData() == meta2.getCustomModelData();
    }

    static boolean checkEnchants(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getEnchants().equals(meta2.getEnchants());
    }

    static boolean hasBlockData(ItemMeta meta) {
        if(!META.getOwner().isAssignableFrom(meta.getClass())) {
            return false;
        }
        return meta instanceof BlockDataMeta && ((BlockDataMeta) meta).hasBlockData();
    }

    static boolean checkBlockData(ItemMeta meta1, ItemMeta meta2) {
        Object tag1 = META.getFieldValue(meta1, "blockData");
        Object tag2 = META.getFieldValue(meta2, "blockData");
        return (tag1 == null || tag2 == null) ? (tag1 == tag2) : tag1.equals(tag2);
    }

    static boolean hasRepairCost(ItemMeta meta) {
        return meta instanceof Repairable && ((Repairable) meta).hasRepairCost();
    }

    static boolean checkRepairCost(ItemMeta meta1, ItemMeta meta2) {
        return ((Repairable) meta1).getRepairCost() == ((Repairable) meta2).getRepairCost();
    }

    static boolean checkAttributeModifiers(ItemMeta meta1, ItemMeta meta2) {
        return Objects.equals(meta1.getAttributeModifiers(), meta2.getAttributeModifiers());
    }

    static boolean hasDamage(ItemMeta meta) {
        return meta instanceof Damageable && ((Damageable) meta).hasDamage();
    }

    static boolean checkDamage(ItemMeta meta1, ItemMeta meta2) {
        return ((Damageable) meta1).getDamage() == ((Damageable) meta2).getDamage();
    }

    static boolean hasFlags(ItemMeta meta) {
        return !meta.getItemFlags().isEmpty();
    }

    static boolean checkFlags(ItemMeta meta1, ItemMeta meta2) {
        return Objects.equals(meta1.getItemFlags(), meta2.getItemFlags());
    }

    static boolean hasPersistentData(ItemMeta meta) {
        return !meta.getPersistentDataContainer().getKeys().isEmpty();
    }

    static boolean checkPersistentData(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getPersistentDataContainer().equals(meta2.getPersistentDataContainer());
    }
    
    static boolean hasUncommon(ItemMeta meta) {
        return META.getOwner().isAssignableFrom(meta.getClass());
    }

    static boolean checkUncommon(ItemMeta meta1, ItemMeta meta2) {
        Boolean state1 = (Boolean) META.run(meta1, "compare", meta2);
        Boolean state2 = (Boolean) META.run(meta2, "compare", meta1);
        return (state1 == null ? true : state1) && (state2 == null ? true : state2);
    }

}
