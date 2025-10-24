package net.glasslauncher.mods.glassguistest.events.init;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;

public class TestBlockGUI extends HandledScreen {
    public TestBlockGUI(PlayerInventory inventory, TestBlockEntity container) {
        super(new TestBlockHandler(inventory, container));
    }

    @Override
    public void drawBackground(float delta) {
        glassguis_renderBackground(this);
        glassguis_drawSlots(this);
    }
}