package net.glasslauncher.mods.glassguis.mixin;

import net.glasslauncher.mods.glassguis.GGUIUtil;
import net.glasslauncher.mods.glassguis.screen.AutoSyncingScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void iHateHowThisIsDone(CallbackInfo ci) {
        GGUIUtil.checkAndCacheFields((BlockEntity) (Object) this);
    }
}