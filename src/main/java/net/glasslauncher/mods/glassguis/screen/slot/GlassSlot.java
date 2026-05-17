package net.glasslauncher.mods.glassguis.screen.slot;

public interface GlassSlot {
    int getWidth();
    int getHeight();

    default boolean keepBackgroundTexture() {
        return false;
    }

    default int getBackgroundWidth() {
        return getWidth();
    }

    default int getBackgroundHeight() {
        return getHeight();
    }

    default boolean shouldScaleItem() {
        return false;
    }

    default boolean renderExtras() {
        return false;
    }
}
