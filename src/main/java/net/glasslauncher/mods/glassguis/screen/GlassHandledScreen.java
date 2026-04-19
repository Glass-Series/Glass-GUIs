package net.glasslauncher.mods.glassguis.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.modificationstation.stationapi.api.util.Util;

import java.awt.*;
import java.util.List;

public interface GlassHandledScreen extends GlassScreen {
    default void glassguis_renderBackground() { Util.assertImpl(); }

    default void glassguis_drawSlots() { Util.assertImpl(); }

    default void glassguis_drawText(String text, int x, int y, int color) { Util.assertImpl(); }

    default void glassguis_tooltip(List<String> text, Rectangle location, int mouseX, int mouseY) { Util.assertImpl(); }

}
