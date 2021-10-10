package org.playuniverse.minecraft.core.lithos.feature.event;

import org.playuniverse.minecraft.mcs.shaded.syapi.random.NumberGeneratorType;
import org.playuniverse.minecraft.mcs.shaded.syapi.random.RandomNumberGenerator;

public final class EventInfo {

    private final RandomNumberGenerator seedGenerator;
    private int tickTime = 1; // Time for ticks in 50ms ticks

    public EventInfo(final RandomNumberGenerator seedGenerator) {
        this.seedGenerator = seedGenerator;
    }

    public RandomNumberGenerator newRandom(final NumberGeneratorType type) {
        return type.create(seedGenerator.nextLong());
    }

    public long newSeed() {
        return seedGenerator.nextLong();
    }

    public void setTickTime(final int tickTime) {
        this.tickTime = Math.max(1, tickTime);
    }

    public int getTickTime() {
        return tickTime;
    }

}
