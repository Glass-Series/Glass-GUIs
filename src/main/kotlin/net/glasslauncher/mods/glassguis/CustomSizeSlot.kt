package net.glasslauncher.mods.glassguis

import net.modificationstation.stationapi.api.client.texture.Sprite
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas

interface CustomSizeSlot {
    fun getWidth(): Int
    fun getHeight(): Int

    fun keepBackgroundTexture(): Boolean {
        return false
    }

    fun getBackgroundWidth(): Int {
        return getWidth()
    }

    fun getBackgroundHeight(): Int {
        return getHeight()
    }

    fun shouldScaleItem(): Boolean {
        return false
    }

    fun getBackgroundSprite(): Atlas.Sprite? {
        return null
    }
}