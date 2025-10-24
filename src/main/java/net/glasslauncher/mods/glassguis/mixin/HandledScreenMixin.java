package net.glasslauncher.mods.glassguis.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.glassguis.screen.widget.slot.CustomSizeSlot;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Unique
    private Slot slot;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/screen/slot/Slot;)V"))
    private void captureSlot(int mouseY, int delta, float par3, CallbackInfo ci, @Local Slot slot) {
        this.slot = slot;
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;fillGradient(IIIIII)V", ordinal = 0))
    private void hijackSlot(HandledScreen instance, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, Operation<Void> original) {
        if (slot instanceof CustomSizeSlot customSizeSlot) {
            int xDiff = (customSizeSlot.getWidth() - 16) / 2;
            int yDiff = (customSizeSlot.getHeight() - 16) / 2;
            startX -= xDiff;
            startY -= yDiff;
            endX += xDiff;
            endY += yDiff;
        }

        original.call(instance, startX, startY, endX, endY, colorStart, colorEnd);
    }

    @WrapOperation(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getBackgroundTextureId()I"))
    private int yeetOldSlotBackground(Slot instance, Operation<Integer> original) {
        if (slot instanceof CustomSizeSlot customSizeSlot && customSizeSlot.getBackgroundSprite() != null) {
            Sprite sprite = customSizeSlot.getBackgroundSprite().getSprite();
            SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(sprite.getAtlasId());
            atlas.bindTexture();
            drawTexture(slot.x, slot.y, 16, 16, sprite);
            return -1;
        }
        return original.call(instance);
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 0))
    private int slotHitbox1(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof CustomSizeSlot customSizeSlot ? value + ((customSizeSlot.getWidth() - 16) / 2) : value;
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 1))
    private int slotHitbox2(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof CustomSizeSlot customSizeSlot ? value + ((customSizeSlot.getWidth() - 16) / 2) : value;
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 2))
    private int slotHitbox3(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof CustomSizeSlot customSizeSlot ? value + ((customSizeSlot.getHeight() - 16) / 2) : value;
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 3))
    private int slotHitbox4(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof CustomSizeSlot customSizeSlot ? value + ((customSizeSlot.getHeight() - 16) / 2) : value;
    }

    private void drawTexture(int x, int y, int width, int height, Sprite sprite) {
        double startU = sprite.getMinU();
        double startV = sprite.getMinV();
        double u = sprite.getMaxU();
        double v = sprite.getMaxV();
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        tessellator.vertex((x + 0D), (y + height), 0.0, startU, v); // bl
        tessellator.vertex((x + width), (y + height), 0.0, u, v); // br
        tessellator.vertex((x + width), (y + 0D), 0.0, u, startV); // tr
        tessellator.vertex((x + 0D), (y + 0D), 0.0, startU, startV); // tl
        tessellator.draw();
    }

//    @WrapOperation(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItem(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemStack;II)V"))
//    private void hijackItem(ItemRenderer instance, TextRenderer textRenderer, TextureManager textureManager, ItemStack stack, int x, int y, Operation<Void> original) {
//        if (slot instanceof CustomSizeSlot customSizeSlot && customSizeSlot.shouldScaleItem()) {
//            // TODO and probably WONTDO: Figure out arsenic hellcode
//        }
//        original.call(instance, textRenderer, textureManager, stack, x, y);
//    }
}
