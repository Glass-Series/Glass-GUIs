package net.glasslauncher.mods.glassguis.screen;

import net.glasslauncher.mods.glassguis.DrawDirection;
import net.glasslauncher.mods.glassguis.screen.widget.GlassWidget;
import net.modificationstation.stationapi.api.util.Util;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public interface GlassScreen {
    default void glassguis_renderBackground(int screenWidth, int screenHeight, int backgroundWidth, int backgroundHeight) { Util.assertImpl(); }

    default void glassguis_drawBackgroundBox(int x, int y, int x2, int y2) { Util.assertImpl(); }

    default void glassguis_fillInclusive(int x, int y, int x2, int y2, int color) { Util.assertImpl(); }

    default void glassguis_drawImage(String imageString, int x, int y) { Util.assertImpl(); }

    default void glassguis_drawImagePercentage(String imageString, int x, int y, float percentage, DrawDirection drawDirection) { Util.assertImpl(); }

    default void glassguis_drawTexture(int x, int y, int width, int height) { Util.assertImpl(); }

    default void glassguis_drawTexture(int x, int y, int width, int height, int imgWidth, int imgHeight) { Util.assertImpl(); }

    default void glassguis_drawTexture(int x, int y, int width, int height, int imgWidth, int imgHeight, int startX, int startY) { Util.assertImpl(); }

    default int glassguis_getGuiBorder() { Util.assertImpl(); return 0; }

    default int glassguis_getGuiRoundingLight() { Util.assertImpl(); return 0; }

    default int glassguis_getGuiRoundingDark() { Util.assertImpl(); return 0; }

    default int glassguis_getGuiBackground() { Util.assertImpl(); return 0; }

    default int glassguis_getSlotRoundingLight() { Util.assertImpl(); return 0;}

    default int glassguis_getSlotRoundingDark() { Util.assertImpl(); return 0; }

    default int glassguis_getSlotBackground() { Util.assertImpl(); return 0; }

    default int glassguis_getTextColor() { Util.assertImpl(); return 0; }

    default String glassguis_getName() { Util.assertImpl(); return null; }

    default void glassguis_setGuiBorder(int guiBorder) { Util.assertImpl(); }

    default void glassguis_setGuiRoundingLight(int guiRoundingLight) { Util.assertImpl(); }

    default void glassguis_setGuiRoundingDark(int guiRoundingDark) { Util.assertImpl(); }

    default void glassguis_setGuiBackground(int guiBackground) { Util.assertImpl(); }

    default void glassguis_setSlotRoundingLight(int slotRoundingLight) { Util.assertImpl(); }

    default void glassguis_setSlotRoundingDark(int slotRoundingDark) { Util.assertImpl(); }

    default void glassguis_setSlotBackground(int slotBackground) { Util.assertImpl(); }

    default void glassguis_setTextColor(int textColor) { Util.assertImpl(); }

    default void glassguis_setName(String name) { Util.assertImpl(); }

    default void glassguis_mouseScrolled(int mouseX, int mouseY, int deltaWheel) { Util.assertImpl(); }

    default List<GlassWidget> glassguis_getWidgets() { Util.assertImpl(); return null; }

    default void glassguis_addWidget(GlassWidget widget) { Util.assertImpl(); }
}
