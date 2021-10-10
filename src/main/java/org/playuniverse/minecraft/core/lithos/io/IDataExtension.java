package org.playuniverse.minecraft.core.lithos.io;

import java.util.List;
import java.util.Optional;

import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.mcs.spigot.module.extension.helper.ExtensionHelper;

import com.syntaxphoenix.avinity.module.extension.ExtensionPoint;
import com.syntaxphoenix.avinity.module.extension.IExtension;

@ExtensionPoint
public interface IDataExtension<I, O> extends IExtension {

    O convert(I input);

    @SuppressWarnings("rawtypes")
    public static int[] register(Lithos lithos) {
        List<IDataExtension> extensions = lithos.getModuleManager().getExtensionManager().getExtensions(IDataExtension.class);
        int[] output = new int[2];
        output[1] = extensions.size();
        if (output[1] == 0) {
            output[0] = 0;
            return output;
        }
        int registered = 0;
        IOHandler handler = lithos.getIOHandler();
        for (IDataExtension<?, ?> extension : extensions) {
            Optional<TypeId> idOption = ExtensionHelper.getAnnotation(extension.getClass(), TypeId.class);
            if (idOption.isEmpty()) {
                continue;
            }
            DataInfo info = new DataInfo(idOption.get(), extension);
            if (info.isValid() && handler.register(info)) {
                registered++;
                continue;
            }
        }
        output[0] = registered;
        return output;
    }

}
