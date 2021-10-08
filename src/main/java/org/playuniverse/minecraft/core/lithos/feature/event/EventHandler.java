package org.playuniverse.minecraft.core.lithos.feature.event;

import java.util.ArrayList;
import java.util.TreeMap;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.feature.Feature;
import org.playuniverse.minecraft.mcs.shaded.syapi.random.NumberGeneratorType;
import org.playuniverse.minecraft.mcs.shaded.syapi.random.RandomNumberGenerator;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.event.ModuleDisableEvent;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo(bukkit = false)
public final class EventHandler extends Feature implements IListenerExtension {

    private final TreeMap<Double, Integer> indexMap = new TreeMap<>();
    private final ArrayList<EventWrapper> wrappers = new ArrayList<>();

    private final RandomNumberGenerator eventRandom = NumberGeneratorType.MURMUR.create(System.currentTimeMillis());
    private final RandomNumberGenerator seedRandom = NumberGeneratorType.MURMUR.create(eventRandom.nextLong());

    private double chance = 0.1;
    private int passed = 0;

    public EventHandler(ModuleWrapper<Lithos> wrapper) {
        wrapper.getModule().getFeatureHandler().register(this);
    }

    boolean register(IFeatureEvent event) {
        int size = wrappers.size();
        for (int index = 0; index < size; index++) {
            if (wrappers.get(index).getEvent().getClass() == event.getClass()) {
                return false;
            }
        }
        return wrappers.add(new EventWrapper(event));
    }

    @Override
    protected void onTick(long deltaTime) {
        if (eventRandom.nextDouble(Double.MAX_VALUE) > chance) {
            chance += additive();
            passed++;
            return;
        }
        EventWrapper wrapper = nextEvent(eventRandom);
        if (wrapper == null) {
            passed++;
            return;
        }
        chance = 0.1;
        passed = 0;
        wrapper.start(seedRandom);
    }

    private double additive() {
        double c = Math.pow(passed + 2, 0.05);
        return Math.pow(c, Math.sin(Math.cos(Math.tan(c))));
    }

    private EventWrapper nextEvent(RandomNumberGenerator random) {
        int size = wrappers.size();
        double max = 0;
        for (int index = 0; index < size; index++) {
            EventWrapper wrapper = wrappers.get(index);
            if (wrapper.isRunning()) {
                continue;
            }
            indexMap.put(max, index);
            max += wrapper.getChance();
        }
        int idx = indexMap.floorEntry(random.nextDouble(max)).getValue();
        indexMap.clear();
        for (int index = 0; index < size; index++) {
            if (idx == index) {
                continue;
            }
            wrappers.get(index).pass();
        }
        return wrappers.get(idx);
    }

    /*
     * Unload modules
     */

    @org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler
    public void onModuleDisable(ModuleDisableEvent event) {
        int size = wrappers.size();
        ModuleWrapper<?> wrapper = event.getWrapper();
        for (int index = 0; index < size; index++) {
            EventWrapper eventWrapper = wrappers.get(index);
            if (!wrapper.isFromModule(eventWrapper.getEvent().getClass())) {
                continue;
            }
            index--;
            size--;
            wrappers.remove(eventWrapper);
            if (eventWrapper.isRunning()) {
                eventWrapper.stop();
            }
        }
    }

}
