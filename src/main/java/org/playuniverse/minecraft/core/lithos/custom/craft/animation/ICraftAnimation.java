package org.playuniverse.minecraft.core.lithos.custom.craft.animation;

import org.playuniverse.minecraft.core.lithos.custom.craft.CraftProcess;

public interface ICraftAnimation {
    
    void finish(CraftProcess process);
    
    void step(CraftProcess process);

}
