package org.playuniverse.minecraft.core.lithos.feature.event;

import org.playuniverse.minecraft.mcs.shaded.syapi.random.RandomNumberGenerator;
import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.ITickReceiver;
import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.Ticker;

public final class EventWrapper implements Comparable<EventWrapper>, ITickReceiver {

    private final IFeatureEventExtension event;

    private final String name;
    private final Ticker ticker;

    private double chance;
    private int passed = 0;

    private boolean running = false;

    public EventWrapper(IFeatureEventExtension event) {
        this.event = event;
        this.chance = event.additive(-1);
        this.name = event.getClass().getTypeName();
        this.ticker = new Ticker(name, 50, 100);
        this.ticker.pause();
        this.ticker.add(this);
    }

    public String getName() {
        return name;
    }

    protected IFeatureEventExtension getEvent() {
        return event;
    }

    public double getChance() {
        return chance;
    }

    public int getPassed() {
        return passed;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void onTick(long deltaTime) {
        if (event.isOver()) {
            stop();
            return;
        }
        event.onTick(deltaTime);
    }

    protected void pass() {
        chance += event.additive(passed);
    }

    protected void start(RandomNumberGenerator seedGenerator) {
        if (running) {
            return;
        }
        running = true;
        chance = 0;
        passed = 0;
        EventInfo info = new EventInfo(seedGenerator);
        event.onStart(info);
        ticker.setLength(50 * info.getTickTime());
        ticker.start();
        return;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        ticker.pause();
        event.onStop();
    }

    @Override
    public int compareTo(EventWrapper other) {
        return Double.compare(chance, other.chance);
    }

}
