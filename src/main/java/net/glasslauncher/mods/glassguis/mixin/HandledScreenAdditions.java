package net.glasslauncher.mods.glassguis.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.glassguis.compat.AlwaysMoreItemsCompat;
import net.glasslauncher.mods.glassguis.compat.StationAPICompat;
import net.glasslauncher.mods.glassguis.screen.GlassHandledScreen;
import net.glasslauncher.mods.glassguis.screen.slot.GlassSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;
import java.util.List;

@Mixin(HandledScreen.class)
public class HandledScreenAdditions extends Screen implements GlassHandledScreen {

    @Shadow
    public ScreenHandler container;

    @Shadow
    public int backgroundWidth;

    @Shadow
    public int backgroundHeight;

    @Override
    public void glassguis_drawSlots() {
        if (container == null || container.slots == null) {
            return;
        }
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        for (Object slotObj : container.slots) {
            if (!(slotObj instanceof Slot slot)) {
                continue;
            }
            int slotX = x + slot.x;
            int slotY = y + slot.y;
            int backgroundWidth;
            int backgroundHeight;
            if (slot instanceof GlassSlot customSlot) {
                backgroundWidth = customSlot.getBackgroundWidth();
                backgroundHeight = customSlot.getBackgroundHeight();
            } else {
                backgroundWidth = 16;
                backgroundHeight = 16;
            }
            int slotBackgroundX = slotX - ((backgroundWidth - 16) / 2);
            int slotBackgroundY = slotY - ((backgroundHeight - 16) / 2);

            if (slot.getBackgroundTextureId() != -1 && ((slot instanceof GlassSlot && ((GlassSlot) slot).keepBackgroundTexture()) || !slot.hasStack())) {
                if (FabricLoader.getInstance().isModLoaded("stationapi")) {
                    StationAPICompat.drawSprite(slot, x, y, this);
                }
            }

            fill(slotBackgroundX - 1, slotBackgroundY - 1, slotBackgroundX + backgroundWidth + 1, slotBackgroundY + backgroundHeight + 1, glassguis_getSlotBackground());
            drawHorizontalLine(slotBackgroundX, slotBackgroundX + backgroundWidth - 1, slotBackgroundY - 1, glassguis_getSlotRoundingDark());
            drawVerticalLine(slotBackgroundX - 1, slotBackgroundY - 2, slotBackgroundY + backgroundHeight, glassguis_getSlotRoundingDark());
            drawHorizontalLine(slotBackgroundX, slotBackgroundX + backgroundWidth, slotBackgroundY + backgroundHeight, glassguis_getSlotRoundingLight());
            drawVerticalLine(slotBackgroundX + backgroundWidth, slotBackgroundY - 1, slotBackgroundY + backgroundHeight, glassguis_getSlotRoundingLight());
        }
    }

    @Override
    public void glassguis_drawText(String text, int x, int y, int color) {
        int offsetX = ((width - backgroundWidth) / 2) + x;
        int offsetY = ((height - backgroundHeight) / 2) + y;
        Minecraft.INSTANCE.textRenderer.draw(text, offsetX, offsetY, color);
    }

    @Override
    public void glassguis_tooltip(List<String> text, Rectangle location, int mouseX, int mouseY) {
        int xOffset = (width - backgroundWidth) / 2;
        int yOffset = (height - backgroundHeight) / 2;

        if (!location.contains(mouseX - xOffset, mouseY - yOffset)) {
            return;
        }

        if (FabricLoader.getInstance().isModLoaded("alwaysmoreitems")) { // Use AMI's much better tooltip system if it's installed.
            //noinspection unchecked cry some more
            AlwaysMoreItemsCompat.setTooltip((List<Object>) (Object) text, mouseX, mouseY);
            return;
        }

        // Otherwise we're rawdogging and praying nothing else is trying to draw a tooltip.
        int startX = mouseX - xOffset + 12;
        int startY = mouseY - yOffset - 12;
        int maxLineLength = 0;
        for (String line : text) {
            if (line.length() > maxLineLength) {
                maxLineLength = line.length();
            }
        }
        if (maxLineLength == 0) {
            return;
        }
        fill(
                startX - 3,
                startY - 3,
                startX + maxLineLength + 3,
                startY + 8 + 3,
                -1073741824
        );
        for (String line : text) {
            Minecraft.INSTANCE.textRenderer.drawWithShadow(line, startX, startY, -1);
            startY += 12;
        }
    }

    @Override
    public void glassguis_renderBackground() {
        glassguis_renderBackground(width, height, backgroundWidth, backgroundHeight);
    }
}
