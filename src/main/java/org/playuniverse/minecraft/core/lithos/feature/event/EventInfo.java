package org.playuniverse.minecraft.core.lithos.feature.event;

import org.playuniverse.minecraft.mcs.shaded.syapi.random.NumberGeneratorType;
import org.playuniverse.minecraft.mcs.shaded.syapi.random.RandomNumberGenerator;

public final class EventInfo {

    private final RandomNumberGenerator seedGenerator;
    private int tickTime; // Time for ticks in 50ms ticks

    public EventInfo(RandomNumberGenerator seedGenerator) {
        this.seedGenerator = seedGenerator;
    }

    public RandomNumberGenerator newRandom(NumberGeneratorType type) {
        return type.create(seedGenerator.nextLong());
    }
    
    public long newSeed() {
        return seedGenerator.nextLong();
    }

    public void setTickTime(int tickTime) {
        this.tickTime = tickTime;
    }

    public int getTickTime() {
        return tickTime;
    }

}
