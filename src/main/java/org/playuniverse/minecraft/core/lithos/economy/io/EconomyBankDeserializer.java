package org.playuniverse.minecraft.core.lithos.economy.io;

import java.util.UUID;

import org.playuniverse.minecraft.core.lithos.economy.EconomyBank;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtType;

@Extension
@TypeId(name = "economy_bank", input = NbtCompound.class, output = EconomyBank.class)
public final class EconomyBankDeserializer implements IDataExtension<NbtCompound, EconomyBank> {

    @Override
    public EconomyBank convert(final NbtCompound input) {
        if (!input.hasKey("owner", NbtType.STRING) || !input.hasKey("value", NbtType.LONG)) {
            return null;
        }
        try {
            final UUID uniqueId = UUID.fromString(input.getString("owner"));
            return new EconomyBank(uniqueId).set(input.getLong("value"));
        } catch (final IllegalArgumentException exp) {
            return null;
        }
    }

}
