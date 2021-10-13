package org.playuniverse.minecraft.core.lithos.custom.craft.recipe.io;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.AdvancedIngredient;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.core.lithos.util.bukkit.MetaCheck;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonValue;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.ValueType;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtJsonParser;
import org.playuniverse.minecraft.vcompat.reflection.BukkitConversion;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "ingredient/advanced", input = JsonObject.class, output = AdvancedIngredient.class)
public final class AdvancedIngredientDeserializer implements IDataExtension<JsonObject, AdvancedIngredient> {

    private final BukkitConversion<?> conversion = VersionControl.get().getBukkitConversion();

    @Override
    public AdvancedIngredient convert(JsonObject input) {
        if (!input.has("item", ValueType.OBJECT) || !input.has("amount", ValueType.NUMBER)) {
            return null;
        }
        NbtCompound compound = NbtJsonParser.toNbtCompound((JsonObject) input.get("item"));
        ItemStack itemStack = conversion.itemFromCompound(compound);
        if (itemStack == null) {
            return null; // Invalid data
        }
        ArrayList<MetaCheck> checks = new ArrayList<>();
        if (input.has("checks", ValueType.ARRAY)) {
            JsonArray array = (JsonArray) input.get("checks");
            for (JsonValue<?> value : array) {
                if (!value.hasType(ValueType.STRING)) {
                    continue;
                }
                MetaCheck check = MetaCheck.fromString(value.getValue().toString());
                if (check == null) {
                    continue;
                }
                checks.add(check);
            }
        }
        return new AdvancedIngredient(itemStack, ((Number) input.get("amount").getValue()).intValue(), checks.toArray(MetaCheck[]::new));
    }

}
