package org.playuniverse.minecraft.core.lithos.custom.structure.util;

import java.util.Objects;

import com.google.common.base.Preconditions;

public final class Checks {
    
    private Checks() {}
    
    public static <T> T[] isNotNullOrEmpty(T[] array, String name) {
        Objects.requireNonNull(array, name + " can't be null!");
        Preconditions.checkArgument(array.length != 0, name + " can't be empty!");
        return array;
    }
    
}
