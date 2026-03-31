package net.glasslauncher.mods.glassguis.mixin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.llamalad7.mixinextras.sugar.Local;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.gcapi3.api.CharacterUtils;
import net.glasslauncher.mods.glassguis.DrawDirection;
import net.glasslauncher.mods.glassguis.compat.AlwaysMoreItemsCompat;
import net.glasslauncher.mods.glassguis.compat.StationAPICompat;
import net.glasslauncher.mods.glassguis.screen.GlassScreen;
import net.glasslauncher.mods.glassguis.screen.widget.GlassWidget;
import net.glasslauncher.mods.glassguis.screen.widget.slot.GlassSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.glasslauncher.mods.glassguis.events.init.GlassGUIs.IMAGE_SIZE_CACHE;

@Mixin(Screen.class)
public class InventoryAdditions extends DrawContext implements GlassScreen<Screen> {
    @Unique
    public List<GlassWidget> widgets = new ArrayList<>();

    @Shadow public int width;

    @Shadow public int height;

    @Unique
    public int glassguis_guiBorder;
    @Unique
    public int glassguis_guiRoundingLight;
    @Unique
    public int glassguis_guiRoundingDark;
    @Unique
    public int glassguis_guiBackground;
    @Unique
    public int glassguis_slotRoundingLight;
    @Unique
    public int glassguis_slotRoundingDark;
    @Unique
    public int glassguis_slotBackground;
    @Unique
    public int glassguis_textColor;

    @Unique
    public String glassguis_name;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void colorInit(CallbackInfo ci) {
        glassguis_guiBorder = CharacterUtils.getIntFromColour(new Color(0, 0, 0));
        glassguis_guiRoundingLight = -1;
        glassguis_guiRoundingDark = CharacterUtils.getIntFromColour(new Color(85, 85, 85));
        glassguis_guiBackground = CharacterUtils.getIntFromColour(new Color(198, 198, 198));
        glassguis_slotRoundingLight = glassguis_guiRoundingLight;
        glassguis_slotRoundingDark = CharacterUtils.getIntFromColour(new Color(55, 55, 55));
        glassguis_slotBackground = CharacterUtils.getIntFromColour(new Color(139, 139, 139));
        glassguis_textColor = CharacterUtils.getIntFromColour(new Color(63, 63, 63));
    }

    @Override
    public void glassguis_renderBackground(HandledScreen screen) {
        glassguis_renderBackground(screen.width, screen.height, screen.backgroundWidth, screen.backgroundHeight);
    }

    @Override
    public void glassguis_renderBackground(int screenWidth, int screenHeight, int backgroundWidth, int backgroundHeight) {
        int x = (screenWidth - backgroundWidth) / 2;
        int y = (screenHeight - backgroundHeight) / 2;

        glassguis_drawBackgroundBox(x, y, x + backgroundWidth, y + backgroundHeight);

        if (glassguis_name != null) {
            Minecraft.INSTANCE.textRenderer.draw(glassguis_name, x + 8, y + 6, glassguis_textColor);
        }
    }

    @Override
    public void glassguis_drawBackgroundBox(int x, int y, int x2, int y2) {
        fill(x + 2, y + 2, x2 - 2, y2 - 2, glassguis_guiBackground);

        drawHorizontalLine(x + 2, x2 - 4, y, glassguis_guiBorder); // top
        drawVerticalLine(x2 - 1, y + 2, y2 - 2, glassguis_guiBorder); // right
        drawHorizontalLine(x + 3, x2 - 3, y2 - 1, glassguis_guiBorder); // bottom
        drawVerticalLine(x, y + 1, y2 - 3, glassguis_guiBorder); // left

        glassguis_fillInclusive(x + 1, y + 1, x + 1, y + 1, glassguis_guiBorder); // tl
        glassguis_fillInclusive(x2 - 3, y + 1, x2 - 3, y + 1, glassguis_guiBorder); // tr1
        glassguis_fillInclusive(x2 - 2, y + 2, x2 - 2, y + 2, glassguis_guiBorder); // tr2
        glassguis_fillInclusive(x2 - 2, y2 - 2, x2 - 2, y2 - 2, glassguis_guiBorder); // br
        glassguis_fillInclusive(x + 2, y2 - 2, x + 2, y2 - 2, glassguis_guiBorder); // bl1
        glassguis_fillInclusive(x + 1, y2 - 3, x + 1, y2 - 3, glassguis_guiBorder); // bl2

        // light rounding
        glassguis_fillInclusive(x + 1, y + 2, x + 2, y2 - 4, glassguis_guiRoundingLight); // l
        glassguis_fillInclusive(x + 2, y + 1, x2 - 4, y + 2, glassguis_guiRoundingLight); // t
        glassguis_fillInclusive(x + 3, y + 3, x + 3, y + 3, glassguis_guiRoundingLight);

        // dark rounding
        glassguis_fillInclusive(x2 - 3, y + 3, x2 - 2, y2 - 3, glassguis_guiRoundingDark); // r
        glassguis_fillInclusive(x + 3, y2 - 3, x2 - 3, y2 - 2, glassguis_guiRoundingDark); // b
        glassguis_fillInclusive(x2 - 4, y2 - 4, x2 - 4, y2 - 4, glassguis_guiRoundingDark);
    }

    @Override
    public void glassguis_fillInclusive(int x, int y, int x2, int y2, int color) {
        fill(x, y, x2 + 1, y2 + 1, color);
    }

    @Override
    public void glassguis_drawImage(DrawContext screen, String imageString, int x, int y) {
        glassguis_drawImagePercentage(screen, imageString, x, y, 1f, DrawDirection.UP);
    }

    @Override
    public void glassguis_drawImagePercentage(DrawContext screen, String imageString, int x, int y, float percentage, DrawDirection drawDirection) {
        if (screen instanceof HandledScreen handledScreen) {
            x += ((handledScreen.width - handledScreen.backgroundWidth) / 2);
            y += ((handledScreen.height - handledScreen.backgroundHeight) / 2);
        }

        int[] size = IMAGE_SIZE_CACHE.asMap().computeIfAbsent(imageString, key -> {
            try {
                BufferedImage image = ImageIO.read(getClass().getResourceAsStream(key));
                return new int[]{image.getWidth(), image.getHeight()};
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        int startX = drawDirection.left ? size[0] - (int) (size[0] * percentage) : 0;
        int startY = drawDirection.up ? size[1] - (int) (size[1] * percentage) : 0;
        x += startX;
        y += startY;

        Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId(imageString));
        glassguis_drawTexture(x, y, drawDirection.right || drawDirection.left ? (int) (size[0] * percentage) : size[0], drawDirection.down || drawDirection.up ? (int) (size[1] * percentage) : size[1], size[0], size[1], startX, startY);
    }

    @Override
    public void glassguis_drawSlots(HandledScreen screen) {
        if (screen.container == null || screen.container.slots == null) {
            return;
        }
        int x = (screen.width - screen.backgroundWidth) / 2;
        int y = (screen.height - screen.backgroundHeight) / 2;

        for (Object slotObj : screen.container.slots) {
            if (!(slotObj instanceof Slot slot)) {
                continue;
            }
            int slotX = x + slot.x;
            int slotY = y + slot.y;
            int width;
            int height;
            int backgroundWidth;
            int backgroundHeight;
            if (slot instanceof GlassSlot customSlot) {
                width = customSlot.getWidth();
                height = customSlot.getHeight();
                backgroundWidth = customSlot.getBackgroundWidth();
                backgroundHeight = customSlot.getBackgroundHeight();
            } else {
                width = 16;
                height = 16;
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

            screen.fill(slotBackgroundX - 1, slotBackgroundY - 1, slotBackgroundX + backgroundWidth + 1, slotBackgroundY + backgroundHeight + 1, glassguis_slotBackground);
            screen.drawHorizontalLine(slotBackgroundX, slotBackgroundX + backgroundWidth - 1, slotBackgroundY - 1, glassguis_slotRoundingDark);
            screen.drawVerticalLine(slotBackgroundX - 1, slotBackgroundY - 2, slotBackgroundY + backgroundHeight, glassguis_slotRoundingDark);
            screen.drawHorizontalLine(slotBackgroundX, slotBackgroundX + backgroundWidth, slotBackgroundY + backgroundHeight, glassguis_slotRoundingLight);
            screen.drawVerticalLine(slotBackgroundX + backgroundWidth, slotBackgroundY - 1, slotBackgroundY + backgroundHeight, glassguis_slotRoundingLight);
        }
    }

    @Override
    public void glassguis_drawTexture(int x, int y, int width, int height) {
        glassguis_drawTexture(x, y, width, height, width, height, 0, 0);
    }

    @Override
    public void glassguis_drawTexture(int x, int y, int width, int height, int imgWidth, int imgHeight) {
        glassguis_drawTexture(x, y, width, height, imgWidth, imgHeight, 0, 0);
    }

    @Override
    public void glassguis_drawTexture(int x, int y, int width, int height, int imgWidth, int imgHeight, int startX, int startY) {
        double startU = (1.0 / imgWidth) * startX;
        double startV = (1.0 / imgHeight) * startY;
        double u = startU + ((1.0 / imgWidth) * width);
        double v = startV + ((1.0 / imgHeight) * height);
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        tessellator.vertex(x, y + height, 0.0, startU, v); // bl
        tessellator.vertex(x + width, y + height, 0.0, u, v); // br
        tessellator.vertex(x + width, y, 0.0, u, startV); // tr
        tessellator.vertex(x, y, 0.0, startU, startV); // tl
        tessellator.draw();
    }

    @Override
    public void glassguis_drawText(HandledScreen screen, String text, int x, int y, int color) {
        int offsetX = ((screen.width - screen.backgroundWidth) / 2) + x;
        int offsetY = ((screen.height - screen.backgroundHeight) / 2) + y;
        Minecraft.INSTANCE.textRenderer.draw(text, offsetX, offsetY, color);
    }

    @Override
    public void glassguis_tooltip(HandledScreen screen, List<String> text, Rectangle location, int mouseX, int mouseY) {
        int xOffset = (screen.width - screen.backgroundWidth) / 2;
        int yOffset = (screen.height - screen.backgroundHeight) / 2;

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
        screen.fill(
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
    public Screen glassguis_getReal() {
        return GlassScreen.super.glassguis_getReal();
    }

    @Override
    public void glassguis_mouseScrolled(int mouseX, int mouseY, int deltaWheel) {
        widgets.forEach(e -> e.onMouseScroll(mouseX, mouseY, deltaWheel));
    }

    @Override
    public List<GlassWidget> glassguis_getWidgets() {
        return widgets;
    }

    @Override
    public void glassguis_addWidget(GlassWidget widget) {
        widgets.add(widget);
    }

    @Inject(method = "onMouseEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseClicked(III)V"))
    public void onMouseDown(CallbackInfo ci, @Local(index = 1) int mouseX, @Local(index = 2) int mouseY) {
        widgets.forEach(e -> {
            if (e.getBounds().contains(mouseX, mouseY)) {
                e.onMouseDown(mouseX, mouseY, Mouse.getEventButton());
            }
        });
    }

    @Inject(method = "onMouseEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseReleased(III)V"))
    public void onMouseUp(CallbackInfo ci, @Local(index = 1) int mouseX, @Local(index = 2) int mouseY) {
        widgets.forEach(e -> e.onMouseUp(mouseX, mouseY, Mouse.getEventButton()));
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderWidgets(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        widgets.forEach(e -> e.render(mouseX, mouseY, delta));

    }

    public int glassguis_getGuiBorder() {
        return glassguis_guiBorder;
    }

    public void glassguis_setGuiBorder(int glassguis_guiBorder) {
        this.glassguis_guiBorder = glassguis_guiBorder;
    }

    public int glassguis_getGuiRoundingLight() {
        return glassguis_guiRoundingLight;
    }

    public void glassguis_setGuiRoundingLight(int glassguis_guiRoundingLight) {
        this.glassguis_guiRoundingLight = glassguis_guiRoundingLight;
    }

    public int glassguis_getGuiRoundingDark() {
        return glassguis_guiRoundingDark;
    }

    public void glassguis_setGuiRoundingDark(int glassguis_guiRoundingDark) {
        this.glassguis_guiRoundingDark = glassguis_guiRoundingDark;
    }

    public int glassguis_getGuiBackground() {
        return glassguis_guiBackground;
    }

    public void glassguis_setGuiBackground(int glassguis_guiBackground) {
        this.glassguis_guiBackground = glassguis_guiBackground;
    }

    public int glassguis_getSlotRoundingLight() {
        return glassguis_slotRoundingLight;
    }

    public void glassguis_setSlotRoundingLight(int glassguis_slotRoundingLight) {
        this.glassguis_slotRoundingLight = glassguis_slotRoundingLight;
    }

    public int glassguis_getSlotRoundingDark() {
        return glassguis_slotRoundingDark;
    }

    public void glassguis_setSlotRoundingDark(int glassguis_slotRoundingDark) {
        this.glassguis_slotRoundingDark = glassguis_slotRoundingDark;
    }

    public int glassguis_getSlotBackground() {
        return glassguis_slotBackground;
    }

    public void glassguis_setSlotBackground(int glassguis_slotBackground) {
        this.glassguis_slotBackground = glassguis_slotBackground;
    }

    public int glassguis_getTextColor() {
        return glassguis_textColor;
    }

    public void glassguis_setTextColor(int glassguis_textColor) {
        this.glassguis_textColor = glassguis_textColor;
    }

    public String glassguis_getName() {
        return glassguis_name;
    }

    public void glassguis_setName(String glassguis_name) {
        this.glassguis_name = glassguis_name;
    }
}