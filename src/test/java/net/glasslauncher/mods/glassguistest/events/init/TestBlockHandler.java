package net.glasslauncher.mods.glassguistest.events.init;

import net.glasslauncher.mods.glassguis.screen.GlassScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

public class TestBlockHandler extends ScreenHandler implements GlassScreenHandler {
    public PlayerInventory playerInventory;
    public TestBlockEntity guiTestBlock;

    public TestBlockHandler(PlayerInventory playerInventory, TestBlockEntity guiTestBlock) {
        this.playerInventory = playerInventory;
        this.guiTestBlock = guiTestBlock;
        addSlot(new Slot(guiTestBlock, 0, 10, 40));
        addSlot(new BatterySlot(guiTestBlock, 1, 32, 40));
        glassguis_setupPlayerInventory(8, 167, playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return guiTestBlock.canPlayerUse(player);
    }

    @Override
    public void glassguis_setProperty(int syncID, long value) {

    }

    @Override
    public void addListener(ScreenHandlerListener listener) {
        super.addListener(listener);
    }
}