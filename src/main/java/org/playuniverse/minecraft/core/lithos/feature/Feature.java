package org.playuniverse.minecraft.core.lithos.feature;

public abstract class Feature {

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

}
