package net.glasslauncher.mods.glassguis

import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot

open class BigSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y), CustomSizeSlot {
    override fun getWidth(): Int {
        return 16
    }

    override fun getHeight(): Int {
        return 16
    }

    override fun getBackgroundWidth(): Int {
        return 24
    }

    override fun getBackgroundHeight(): Int {
        return 24
    }
}