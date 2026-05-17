package net.glasslauncher.mods.glassguis.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.glassguis.screen.slot.GlassSlot;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("LocalMayUseName") // Bad advice
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
        if (slot instanceof GlassSlot glassSlot) {
            int xDiff = (glassSlot.getWidth() - 16) / 2;
            int yDiff = (glassSlot.getHeight() - 16) / 2;
            startX -= xDiff;
            startY -= yDiff;
            endX += xDiff;
            endY += yDiff;
        }

        original.call(instance, startX, startY, endX, endY, colorStart, colorEnd);
    }

    @WrapOperation(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getBackgroundTextureId()I"))
    private int yeetOldSlotBackground(Slot instance, Operation<Integer> original) {
        if (slot instanceof GlassSlot glassSlot && glassSlot.renderExtras()) {
            return -1;
        }
        return original.call(instance);
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 0))
    private int slotHitbox1(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof GlassSlot glassSlot ? value + ((glassSlot.getWidth() - 16) / 2) : value;
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 1))
    private int slotHitbox2(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof GlassSlot glassSlot ? value + ((glassSlot.getWidth() - 16) / 2) : value;
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 2))
    private int slotHitbox3(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof GlassSlot glassSlot ? value + ((glassSlot.getHeight() - 16) / 2) : value;
    }

    @ModifyConstant(method = "isPointOverSlot", constant = @Constant(intValue = 1, ordinal = 3))
    private int slotHitbox4(int value, @Local(argsOnly = true) Slot slot) {
        return slot instanceof GlassSlot glassSlot ? value + ((glassSlot.getHeight() - 16) / 2) : value;
    }

//    @WrapOperation(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItem(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemStack;II)V"))
//    private void hijackItem(ItemRenderer instance, TextRenderer textRenderer, TextureManager textureManager, ItemStack stack, int x, int y, Operation<Void> original) {
//        if (slot instanceof CustomSizeSlot customSizeSlot && customSizeSlot.shouldScaleItem()) {
//            // TODO and probably WONTDO: Figure out arsenic hellcode
//        }
//        original.call(instance, textRenderer, textureManager, stack, x, y);
//    }
}
