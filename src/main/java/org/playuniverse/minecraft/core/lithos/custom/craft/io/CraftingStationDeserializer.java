package org.playuniverse.minecraft.core.lithos.custom.craft.io;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.craft.CraftingStation;
import org.playuniverse.minecraft.core.lithos.custom.craft.animation.BasicCraftAnimation;
import org.playuniverse.minecraft.core.lithos.custom.craft.animation.ICraftAnimation;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.IRecipe;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.ModuleWrapper;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonValue;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.ValueType;

@Extension
@TypeId(name = "crafting_station", input = JsonObject.class, output = CraftingStation.class)
public final class CraftingStationDeserializer implements IDataExtension<JsonObject, CraftingStation> {

    private final IOHandler ioHandler;

    public CraftingStationDeserializer(ModuleWrapper<Lithos> wrapper) {
        this.ioHandler = wrapper.getModule().getIOHandler();
    }

    @Override
    public CraftingStation convert(JsonObject input) {
        if (!input.has("name", ValueType.STRING)) {
            return null;
        }
        int time = input.has("time", ValueType.NUMBER) ? Math.abs(((Number) input.get("time").getValue()).intValue()) : 0;
        CraftingStation station = new CraftingStation(input.get("name").getValue().toString(), time);
        if (input.has("recipes", ValueType.ARRAY)) {
            JsonArray array = (JsonArray) input.get("recipes");
            for (JsonValue<?> value : array) {
                if (!value.hasType(ValueType.OBJECT)) {
                    continue;
                }
                Object object = ioHandler.deserializeJson((JsonObject) value);
                if (object == null || !(object instanceof IRecipe)) {
                    continue;
                }
                station.add((IRecipe) object);
            }
        }
        if (input.has("animations", ValueType.ARRAY)) {
            JsonArray array = (JsonArray) input.get("animations");
            for (JsonValue<?> value : array) {
                if (!value.hasType(ValueType.OBJECT)) {
                    continue;
                }
                Object object = ioHandler.deserializeJson((JsonObject) value);
                if (object == null || !(object instanceof ICraftAnimation)) {
                    continue;
                }
                station.add((ICraftAnimation) object);
            }
        }
        if (station.getAnimations().length == 0) {
            station.add(new BasicCraftAnimation());
        }
        return station;
    }

}