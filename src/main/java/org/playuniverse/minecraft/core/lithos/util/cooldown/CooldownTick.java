package org.playuniverse.minecraft.core.lithos.util.cooldown;

import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.ITickReceiver;

final class CooldownTick<E> implements ITickReceiver {

    private final CooldownQueue<E> queue;

    public CooldownTick(CooldownQueue<E> queue) {
        this.queue = queue;
    }

    @Override
    public void onTick(long deltaTime) {
        queue.update();
    }

}