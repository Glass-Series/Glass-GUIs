package net.glasslauncher.mods.glassguistest.events.init

import net.glasslauncher.mods.glassguis.InventoryAdditions
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler

class GuiTestGui(val playerInventory: PlayerInventory, container: GuiTestBlockEntity) : HandledScreen(GuiTestHandler(playerInventory, container)) {
    companion object {
        val additions = InventoryAdditions()
    }

    override fun render(mouseX: Int, mouseY: Int, delta: Float) {
        super.render(mouseX, mouseY, delta)
    }

    override fun drawBackground(tickDelta: Float) {
        additions.renderBackground(this)
        additions.drawSlots(this)
    }
}