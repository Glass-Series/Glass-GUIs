package net.glasslauncher.mods.glassguistest.events.init

import net.glasslauncher.mods.glassguis.BigHitboxSlot
import net.minecraft.inventory.Inventory
import net.modificationstation.stationapi.api.client.texture.Sprite
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas

class BatterySlot(inventory: Inventory, index: Int, x: Int, y: Int) : BigHitboxSlot(inventory, index, x, y) {

    override fun getBackgroundSprite(): Atlas.Sprite? {
        return GlassGUIsTest.sprite
    }
}