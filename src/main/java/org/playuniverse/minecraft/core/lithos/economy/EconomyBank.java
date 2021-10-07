package org.playuniverse.minecraft.core.lithos.economy;

import java.io.File;
import java.util.UUID;

public final class EconomyBank {

    private final UUID uniqueId;
    private final File file;

    private long value;

    public EconomyBank(File folder, UUID uniqueId) {
        this.file = new File(folder, uniqueId.toString() + ".nbt");
        this.uniqueId = uniqueId;
    }

    public File getFile() {
        return file;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public long get() {
        return value;
    }

    public EconomyBank set(long value) {
        this.value = value;
        return this;
    }

    public EconomyBank add(long value) {
        this.value += value;
        return this;
    }

    public EconomyBank subtract(long value) {
        this.value -= value;
        return this;
    }

}
