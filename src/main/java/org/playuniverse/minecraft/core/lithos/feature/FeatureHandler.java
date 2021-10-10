package org.playuniverse.minecraft.core.lithos.feature;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.ITickReceiver;
import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.Ticker;

import com.syntaxphoenix.avinity.module.ModuleWrapper;

public final class FeatureHandler implements ITickReceiver {

    private final ConcurrentHashMap<Class<?>, Feature> features = new ConcurrentHashMap<>();
    private final Ticker ticker = new Ticker("Feature Tick", 50, 0);

    public FeatureHandler() {
        ticker.add(this);
    }

    public boolean register(final Feature feature) {
        if (feature == null || features.containsKey(feature.getClass())) {
            return false;
        }
        features.put(feature.getClass(), feature);
        return true;
    }

    public <E extends Feature> E get(final Class<E> clazz) {
        final Feature feature = features.get(clazz);
        return feature == null ? null : clazz.cast(feature);
    }

    @Override
    public void onTick(final long deltaTime) {
        for (final Feature feature : features.values()) {
            feature.tick(deltaTime);
        }
    }

    public void unregister(final ModuleWrapper<?> wrapper) {
        final Entry<?, ?>[] entries = features.entrySet().toArray(Entry[]::new);
        for (final Entry<?, ?> entry : entries) {
            if (!wrapper.isFromModule((Class<?>) entry.getKey())) {
                continue;
            }
            features.remove(entry.getKey()).setEnabled(false);
        }
        for (final Feature feature : features.values()) {
            feature.onModuleDisable(wrapper);
        }
    }

}
