package org.playuniverse.minecraft.core.lithos.custom.craft.recipe.io;

import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.BasicIngredient;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "ingredient/basic", input = BasicIngredient.class, output = JsonObject.class)
public final class BasicIngredientSerializer implements IDataExtension<BasicIngredient, JsonObject> {

    @Override
    public JsonObject convert(BasicIngredient input) {
        JsonObject object = new JsonObject();
        object.set("material", input.getMaterial().name().toLowerCase());
        object.set("amount", input.getAmount());
        object.set("durability", !input.isDurabilityIgnored());
        return object;
    }

}
