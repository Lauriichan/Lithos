package org.playuniverse.minecraft.core.lithos.custom.craft;

import org.bukkit.Location;
import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.ITickReceiver;

final class CraftTicker implements ITickReceiver {

    private final CraftingListener handle;

    public CraftTicker(CraftingListener handle) {
        this.handle = handle;
    }

    @Override
    public void onTick(long deltaTime) {
        Location[] locations = handle.stations.toArray(Location[]::new);
        for (Location location : locations) {
            CraftProcess process = handle.processes.get(location);
            if (process.step()) {
                continue;
            }
            handle.stations.remove(location);
            handle.processes.remove(location);
        }
    }

}
