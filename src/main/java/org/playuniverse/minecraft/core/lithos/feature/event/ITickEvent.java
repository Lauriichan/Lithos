package org.playuniverse.minecraft.core.lithos.feature.event;

import java.util.List;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.mcs.spigot.utils.general.tick.ITickReceiver;

import com.syntaxphoenix.avinity.module.extension.ExtensionPoint;
import com.syntaxphoenix.avinity.module.extension.IExtension;

@ExtensionPoint
public interface ITickEvent extends IExtension, ITickReceiver {

    double additive(int passed);

    void onStart(EventInfo info);

    void onTick(long deltaTime);

    void onStop();

    public static int[] register(Lithos lithos) {
        List<ITickEvent> events = lithos.getModuleManager().getExtensionManager().getExtensions(ITickEvent.class);
        int[] output = new int[2];
        output[1] = events.size();
        if (output[1] == 0) {
            output[0] = 0;
            return output;
        }
        int registered = 0;
        EventHandler handler = lithos.getFeatureHandler().get(EventHandler.class);
        for (ITickEvent event : events) {
            if (!handler.register(event)) {
                continue;
            }
            registered++;
        }
        output[0] = registered;
        return output;
    }

}
