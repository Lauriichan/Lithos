package org.playuniverse.minecraft.core.lithos.custom.craft.recipe.io;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.IIngredient;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.UnshapedRecipe;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.ModuleWrapper;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtJsonParser;
import org.playuniverse.minecraft.vcompat.reflection.BukkitConversion;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;

@Extension
@TypeId(name = "recipe/unshaped", input = UnshapedRecipe.class, output = JsonObject.class)
public final class UnshapedRecipeSerializer implements IDataExtension<UnshapedRecipe, JsonObject> {

    private final BukkitConversion<?> conversion = VersionControl.get().getBukkitConversion();
    private final IOHandler ioHandler;

    public UnshapedRecipeSerializer(ModuleWrapper<Lithos> wrapper) {
        this.ioHandler = wrapper.getModule().getIOHandler();
    }

    @Override
    public JsonObject convert(UnshapedRecipe input) {
        JsonObject object = new JsonObject();
        object.set("result", NbtJsonParser.toJsonObject(conversion.itemToCompound(input.getResult())));
        JsonArray array = new JsonArray();
        for (IIngredient ingredient : input.getIngredients()) {
            JsonObject jsonObject = ioHandler.serializeJson(ingredient);
            if (jsonObject == null) {
                continue; // Unsupported ingradient
            }
            array.add(jsonObject);
        }
        object.set("ingredients", array);
        return object;
    }

}
