package org.playuniverse.minecraft.core.lithos.custom.craft.recipe.io;

import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.AdvancedIngredient;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.core.lithos.util.bukkit.MetaCheck;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtJsonParser;
import org.playuniverse.minecraft.vcompat.reflection.BukkitConversion;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "ingredient/advanced", input = AdvancedIngredient.class, output = JsonObject.class)
public final class AdvancedIngredientSerializer implements IDataExtension<AdvancedIngredient, JsonObject> {

    private final BukkitConversion<?> conversion = VersionControl.get().getBukkitConversion();

    @Override
    public JsonObject convert(AdvancedIngredient input) {
        JsonObject object = new JsonObject();
        object.set("item", NbtJsonParser.toJsonObject(conversion.itemToCompound(input.getItem())));
        object.set("amount", input.getAmount());
        JsonArray array = new JsonArray();
        for (MetaCheck check : input.getChecks()) {
            array.add(check.name().toLowerCase());
        }
        object.set("checks", array);
        return object;
    }

}
