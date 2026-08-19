package net.glasslauncher.mods.glassguis.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.mods.glassguis.GGUIUtil;
import net.glasslauncher.mods.glassguis.screen.AutoSyncingScreenHandler;
import net.glasslauncher.mods.glassguis.screen.GlassScreenHandler;
import net.glasslauncher.mods.networking.GlassNetworking;
import net.glasslauncher.mods.networking.GlassPacket;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements GlassScreenHandler {

    @Shadow public abstract void addSlot(Slot slot);

    @Shadow public int syncId;


    @Shadow protected List listeners;

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

    @Environment(EnvType.SERVER)
    @Inject(method = "addListener", at = @At("RETURN"))
    private void handleAutoSend(ScreenHandlerListener par1, CallbackInfo ci) {
        if (this instanceof AutoSyncingScreenHandler) {
            GGUIUtil.handleAutoSend((PlayerEntity) par1, (ScreenHandler) (Object) this);
        }
    }

    @Inject(method = "sendContentUpdates", at = @At("RETURN"))
    private void sendData(CallbackInfo ci) {
        if (this instanceof AutoSyncingScreenHandler) {
            for (Object listener : listeners) {
                if (listener instanceof PlayerEntity player) {
                    GGUIUtil.handleAutoSend(player, (ScreenHandler) (Object) this);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "setProperty", at = @At("RETURN"))
    private void handleAutoInt(int propertyId, int intValue, CallbackInfo ci) {
        Field field = glassguis_getSettableField(propertyId, int.class);
        if (field != null) {
            try {
                field.setInt(((AutoSyncingScreenHandler) this).getBlockEntity(), intValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Field glassguis_getSettableField(int propertyId, Class<?> type)  {
        if (this instanceof AutoSyncingScreenHandler syncingScreenHandler) {
            Field[] fields = GGUIUtil.getSyncedFields(syncingScreenHandler.getBlockEntity().getClass());
            if (fields != null && propertyId < fields.length && fields[propertyId].getType() == type) {
                return fields[propertyId];
            }
        }

        return null;
    }
}
