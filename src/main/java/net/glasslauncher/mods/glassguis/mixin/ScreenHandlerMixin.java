package net.glasslauncher.mods.glassguis.mixin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.glasslauncher.mods.glassguis.FieldConsumer;
import net.glasslauncher.mods.glassguis.Util;
import net.glasslauncher.mods.glassguis.screen.GlassScreenHandler;
import net.glasslauncher.mods.glassguis.screen.SyncedScreenHandlerValue;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.*;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements GlassScreenHandler {

    @Shadow public abstract void addSlot(Slot slot);

    @Override
    public void glassguis_setupPlayerInventory(int x, int y, PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(
                        new Slot(playerInventory, col + row * 9 + 9, x + col * 18, y - (4 - row) * 18 - 11)
                );
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlot(new Slot(playerInventory, hotbarSlot, x + hotbarSlot * 18, y - 25));
        }
    }

//    @Inject(method = "addListener", at = @At("TAIL"))
//    private void iHateHowThisIsDone(ScreenHandlerListener par1, CallbackInfo ci) {
//        Util.iHateHowThisIsDone(par1, (ScreenHandler) (Object) this);
//    }
}
