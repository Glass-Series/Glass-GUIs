package net.glasslauncher.mods.glassguis.screen.widget.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class BigSlot extends Slot implements GlassSlot {

    public BigSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getBackgroundWidth() {
        return 24;
    }

    @Override
    public int getBackgroundHeight() {
        return 24;
    }
}
