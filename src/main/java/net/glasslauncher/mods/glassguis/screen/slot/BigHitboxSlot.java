package net.glasslauncher.mods.glassguis.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class BigHitboxSlot extends Slot implements GlassSlot {

    public BigHitboxSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    @Override
    public boolean shouldScaleItem() {
        return true;
    }
}
