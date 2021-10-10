package org.playuniverse.minecraft.core.lithos.io;

public @interface TypeId {
    
    public String name() default "";
    
    public Class<?> input();
    
    public Class<?> output();

}
