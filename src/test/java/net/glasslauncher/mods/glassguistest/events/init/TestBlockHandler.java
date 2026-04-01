package net.glasslauncher.mods.glassguistest.events.init;

import net.glasslauncher.mods.glassguis.screen.AutoSyncingScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;

public class TestBlockHandler extends ScreenHandler implements AutoSyncingScreenHandler {
    public PlayerInventory playerInventory;
    public TestBlockEntity testBlockEntity;

    public TestBlockHandler(PlayerInventory playerInventory, TestBlockEntity testBlockEntity) {
        this.playerInventory = playerInventory;
        this.testBlockEntity = testBlockEntity;
        addSlot(new Slot(testBlockEntity, 0, 10, 40));
        addSlot(new BatterySlot(testBlockEntity, 1, 32, 40));
        glassguis_setupPlayerInventory(8, 167, playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return testBlockEntity.canPlayerUse(player);
    }

    @Override
    public void addListener(ScreenHandlerListener listener) {
        super.addListener(listener);
    }

    @Override
    public BlockEntity getBlockEntity() {
        return testBlockEntity;
    }
}