package net.glasslauncher.mods.glassguis.screen.widget.slot;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import org.jetbrains.annotations.Nullable;

public interface CustomSizeSlot {
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

    @Nullable
    default Atlas.Sprite getBackgroundSprite() {
        return null;
    }
}
