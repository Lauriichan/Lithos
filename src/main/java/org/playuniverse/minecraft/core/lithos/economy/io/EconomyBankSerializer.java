package org.playuniverse.minecraft.core.lithos.economy.io;

import org.playuniverse.minecraft.core.lithos.economy.EconomyBank;
import org.playuniverse.minecraft.core.lithos.io.IDataExtension;
import org.playuniverse.minecraft.core.lithos.io.TypeId;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@TypeId(name = "economy_bank", input = EconomyBank.class, output = NbtCompound.class)
public final class EconomyBankSerializer implements IDataExtension<EconomyBank, NbtCompound> {

    @Override
    public NbtCompound convert(final EconomyBank input) {
        final NbtCompound compound = new NbtCompound();
        compound.set("owner", input.getUniqueId().toString());
        compound.set("value", input.get());
        return compound;
    }

}
