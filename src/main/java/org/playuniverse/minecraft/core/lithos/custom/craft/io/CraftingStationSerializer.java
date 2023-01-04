package org.playuniverse.minecraft.core.lithos.custom.craft.io;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.craft.CraftingStation;
import org.playuniverse.minecraft.core.lithos.custom.craft.animation.ICraftAnimation;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.IRecipe;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.ModuleWrapper;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;

@Extension
@TypeId(name = "crafting_station", input = CraftingStation.class, output = JsonObject.class)
public final class CraftingStationSerializer implements IDataExtension<CraftingStation, JsonObject> {

    private final IOHandler ioHandler;

    public CraftingStationSerializer(ModuleWrapper<Lithos> wrapper) {
        this.ioHandler = wrapper.getModule().getIOHandler();
    }

    @Override
    public JsonObject convert(CraftingStation input) {
        JsonObject object = new JsonObject();
        object.set("name", input.getName());
        object.set("time", input.getTime());
        JsonArray array = new JsonArray();
        for (IRecipe recipe : input.getRecipes()) {
            JsonObject jsonObject = ioHandler.serializeJson(recipe);
            if (jsonObject == null) {
                continue; // Unsupported recipe
            }
            array.add(jsonObject);
        }
        object.set("recipes", array);
        array = new JsonArray();
        for (ICraftAnimation animation : input.getAnimations()) {
            JsonObject jsonObject = ioHandler.serializeJson(animation);
            if (jsonObject == null) {
                continue; // Unsupported animation
            }
            array.add(jsonObject);
        }
        object.set("animations", array);
        return object;
    }

}
