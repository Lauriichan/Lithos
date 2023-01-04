package org.playuniverse.minecraft.core.lithos.custom.craft.recipe.io;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
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
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonValue;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.ValueType;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtJsonParser;
import org.playuniverse.minecraft.vcompat.reflection.BukkitConversion;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;

@Extension
@TypeId(name = "recipe/unshaped", input = JsonObject.class, output = UnshapedRecipe.class)
public final class UnshapedRecipeDeserializer implements IDataExtension<JsonObject, UnshapedRecipe> {

    private final BukkitConversion<?> conversion = VersionControl.get().getBukkitConversion();
    private final IOHandler ioHandler;

    public UnshapedRecipeDeserializer(ModuleWrapper<Lithos> wrapper) {
        this.ioHandler = wrapper.getModule().getIOHandler();
    }

    @Override
    public UnshapedRecipe convert(JsonObject input) {
        if (!input.has("result", ValueType.OBJECT) || !input.has("ingredients", ValueType.ARRAY)) {
            return null;
        }
        NbtCompound compound = NbtJsonParser.toNbtCompound((JsonObject) input.get("result"));
        ItemStack itemStack = conversion.itemFromCompound(compound);
        if (itemStack == null) {
            return null; // Invalid data
        }
        UnshapedRecipe recipe = new UnshapedRecipe(itemStack);
        ArrayList<IIngredient> ingredients = recipe.getIngredients();
        JsonArray array = (JsonArray) input.get("ingredients");
        for (JsonValue<?> value : array) {
            if (!value.hasType(ValueType.OBJECT)) {
                continue;
            }
            Object object = ioHandler.deserializeJson((JsonObject) value);
            if (object == null || !(object instanceof IIngredient)) {
                continue;
            }
            ingredients.add((IIngredient) object);
        }
        return recipe;
    }

}
