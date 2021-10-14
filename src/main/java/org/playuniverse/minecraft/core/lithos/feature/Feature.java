package org.playuniverse.minecraft.core.lithos.feature;

import java.util.List;

import org.playuniverse.minecraft.core.lithos.Lithos;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.extension.ExtensionPoint;
import com.syntaxphoenix.avinity.module.extension.IExtension;

@ExtensionPoint
public abstract class Feature implements IExtension {

    private boolean enabled = true;

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            onStop();
        }
    }

    final void tick(final long deltaTime) {
        if (!enabled) {
            return;
        }
        onTick(deltaTime);
    }

    protected abstract void onTick(long deltaTime);

    protected void onStop() {}

    protected void onModuleDisable(ModuleWrapper<?> wrapper) {}

    public static int[] register(final Lithos lithos) {
        final List<Feature> features = lithos.getModuleManager().getExtensionManager().getExtensions(Feature.class);
        final int[] output = new int[2];
        output[1] = features.size();
        if (output[1] == 0) {
            output[0] = 0;
            return output;
        }
        int registered = 0;
        final FeatureHandler handler = lithos.getFeatureHandler();
        for (final Feature feature : features) {
            if (!handler.register(feature)) {
                continue;
            }
            registered++;
        }
        output[0] = registered;
        return output;
    }

}
