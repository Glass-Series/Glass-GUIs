package net.glasslauncher.mods.glassguistest.events.init

import net.glasslauncher.mods.glassguis.BigHitboxSlot
import net.glasslauncher.mods.glassguis.ExtendedScreenHandler
import net.glasslauncher.mods.glassguis.InventoryAdditions
import net.glasslauncher.mods.glassguis.ScreenHandlerPropertyUpdateLongS2CPacket
import net.glasslauncher.mods.glassguistest.events.init.GuiTestGui.Companion.additions
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerListener
import net.minecraft.screen.slot.Slot

class GuiTestHandler(val playerInventory: PlayerInventory, val guiTestBlock: GuiTestBlockEntity) : ScreenHandler(), ExtendedScreenHandler {

    init {
        addSlot(Slot(guiTestBlock, 0, 10, 40))
        addSlot(BatterySlot(guiTestBlock, 1, 32, 40))
        additions.setupPlayerInventory(this, playerInventory) // Only actually ran on server
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return guiTestBlock.canPlayerUse(player)
    }

    override fun setProperty(syncID: Int, long: Long) {

    }
}