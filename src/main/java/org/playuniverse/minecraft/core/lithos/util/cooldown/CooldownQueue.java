package org.playuniverse.minecraft.core.lithos.util.cooldown;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.Ticker;

public final class CooldownQueue<E> {

    private static final AtomicInteger ID = new AtomicInteger();

    private final Ticker ticker = new Ticker("Cooldown-" + ID.getAndAdd(1));
    private final CooldownTick<E> tick = new CooldownTick<>(this);

    private final HashMap<E, Integer> ticks = new HashMap<>();

    private int cooldown = 1;

    public CooldownQueue(int time) {
        ticker.setLength(Math.max(10, time));
        ticker.add(tick);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = Math.max(1, cooldown);
    }

    public boolean enqueue(E object) {
        if (ticks.containsKey(object)) {
            return false;
        }
        ticks.put(object, cooldown);
        return true;
    }

    public int get(E object) {
        return ticks.getOrDefault(object, 0);
    }

    @SuppressWarnings("unchecked")
    void update() {
        Object[] array = ticks.keySet().toArray(Object[]::new);
        for (Object object : array) {
            remove((E) object);
        }
    }

    private void remove(E object) {
        int amount = ticks.get(object);
        if (amount - 1 == 0) {
            ticks.remove(object);
            return;
        }
        ticks.put(object, amount - 1);
    }

}
