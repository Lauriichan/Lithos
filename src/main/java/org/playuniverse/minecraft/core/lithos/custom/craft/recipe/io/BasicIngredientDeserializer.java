package org.playuniverse.minecraft.core.lithos.custom.craft.recipe.io;

import org.bukkit.Material;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.BasicIngredient;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.ValueType;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "ingredient/basic", input = JsonObject.class, output = BasicIngredient.class)
public final class BasicIngredientDeserializer implements IDataExtension<JsonObject, BasicIngredient> {

    @Override
    public BasicIngredient convert(JsonObject input) {
        if (!input.has("material", ValueType.STRING) || !input.has("amount", ValueType.NUMBER)) {
            return null;
        }
        Material material;
        try {
            material = Material.valueOf(input.get("material").getValue().toString().toUpperCase());
        } catch (IllegalArgumentException exp) {
            return null;
        }
        boolean ignoreDurability = input.has("durability", ValueType.BOOLEAN) ? !((boolean) input.get("durability").getValue()) : true;
        return new BasicIngredient(material, ((Number) input.get("amount").getValue()).intValue(), ignoreDurability);
    }

}
