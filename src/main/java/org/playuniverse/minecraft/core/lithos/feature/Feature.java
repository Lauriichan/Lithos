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

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    final void tick(long deltaTime) {
        if (!enabled) {
            return;
        }
        onTick(deltaTime);
    }

    protected abstract void onTick(long deltaTime);
    
    protected abstract void onModuleDisable(ModuleWrapper<?> wrapper);

    public static int[] register(Lithos lithos) {
        List<Feature> features = lithos.getModuleManager().getExtensionManager().getExtensions(Feature.class);
        int[] output = new int[2];
        output[1] = features.size();
        if (output[1] == 0) {
            output[0] = 0;
            return output;
        }
        int registered = 0;
        FeatureHandler handler = lithos.getFeatureHandler();
        for (Feature feature : features) {
            if (!handler.register(feature)) {
                continue;
            }
            registered++;
        }
        output[0] = registered;
        return output;
    }

}
