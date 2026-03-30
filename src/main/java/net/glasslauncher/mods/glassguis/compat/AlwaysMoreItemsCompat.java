package net.glasslauncher.mods.glassguis.compat;

import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;

import java.util.List;

public class AlwaysMoreItemsCompat {

    public static void setTooltip(List<Object> tooltip, int x, int y) {
        Tooltip.INSTANCE.setTooltip(tooltip, x, y);
    }
}
