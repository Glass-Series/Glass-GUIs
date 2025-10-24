package net.glasslauncher.mods.glassguistest.events.init;

import net.glasslauncher.mods.glassguis.screen.widget.slot.BigHitboxSlot;
import net.minecraft.inventory.Inventory;

public class BatterySlot extends BigHitboxSlot {

    public BatterySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

}