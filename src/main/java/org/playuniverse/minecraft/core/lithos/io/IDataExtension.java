package org.playuniverse.minecraft.core.lithos.io;

import com.syntaxphoenix.avinity.module.extension.ExtensionPoint;
import com.syntaxphoenix.avinity.module.extension.IExtension;

@ExtensionPoint
public interface IDataExtension<I, O> extends IExtension {
    
    O convert(I input);

}
