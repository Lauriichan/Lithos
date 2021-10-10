package org.playuniverse.minecraft.core.lithos.custom.craft.io;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.craft.CraftingStation;
import org.playuniverse.minecraft.core.lithos.custom.craft.recipe.IRecipe;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonArray;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonValue;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.JsonObject;
import org.playuniverse.minecraft.mcs.shaded.syapi.json.ValueType;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "crafting_station", input = JsonObject.class, output = CraftingStation.class)
public final class CraftingStationDeserializer implements IDataExtension<JsonObject, CraftingStation> {

    private final IOHandler ioHandler;

    public CraftingStationDeserializer(ModuleWrapper<Lithos> wrapper) {
        this.ioHandler = wrapper.getModule().getIOHandler();
    }

    @Override
    public CraftingStation convert(JsonObject input) {
        if (!input.has("name", ValueType.STRING) || !input.has("recipes", ValueType.ARRAY)) {
            return null;
        }
        CraftingStation station = new CraftingStation(input.get("name").getValue().toString());
        JsonArray array = (JsonArray) input.get("recipes");
        for (JsonValue<?> value : array) {
            if (value.getType() != ValueType.OBJECT) {
                continue;
            }
            Object object = ioHandler.deserializeJson((JsonObject) value);
            if (object == null || !(object instanceof IRecipe)) {
                continue;
            }
            station.add((IRecipe) object);
        }
        return station;
    }

}