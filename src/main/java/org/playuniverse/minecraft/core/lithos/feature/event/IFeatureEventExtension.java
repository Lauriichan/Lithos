package org.playuniverse.minecraft.core.lithos.feature.event;

import java.util.List;

import org.playuniverse.minecraft.core.lithos.Lithos;

import com.syntaxphoenix.avinity.module.extension.ExtensionPoint;
import com.syntaxphoenix.avinity.module.extension.IExtension;

@ExtensionPoint
public interface IFeatureEventExtension extends IExtension {

    double additive(int passed);

    void onStart(EventInfo info);

    boolean isOver();
    
    void onTick(long deltaTime);

    void onStop();

    public static int[] register(Lithos lithos) {
        List<IFeatureEventExtension> events = lithos.getModuleManager().getExtensionManager().getExtensions(IFeatureEventExtension.class);
        int[] output = new int[2];
        output[1] = events.size();
        if (output[1] == 0) {
            output[0] = 0;
            return output;
        }
        int registered = 0;
        EventHandler handler = lithos.getFeatureHandler().get(EventHandler.class);
        for (IFeatureEventExtension event : events) {
            if (!handler.register(event)) {
                continue;
            }
            registered++;
        }
        output[0] = registered;
        return output;
    }

}
