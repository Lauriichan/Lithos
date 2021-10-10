package org.playuniverse.minecraft.core.lithos.economy;

import java.util.UUID;

public final class EconomyBank {

    private final UUID uniqueId;
    private long value = 0;

    public EconomyBank(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public long get() {
        return value;
    }

    public EconomyBank set(final long value) {
        this.value = value;
        return this;
    }

    public EconomyBank add(final long value) {
        this.value += value;
        return this;
    }

    public EconomyBank subtract(final long value) {
        this.value -= value;
        return this;
    }

}
