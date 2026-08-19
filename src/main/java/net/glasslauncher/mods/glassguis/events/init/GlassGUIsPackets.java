package net.glasslauncher.mods.glassguis.events.init;

import net.glasslauncher.mods.networking.GlassPacketListener;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;

public class GlassGUIsPackets implements GlassPacketListener {
    @Override
    public void registerGlassPackets() {
        registerGlassPacket("glassguis:int", (p, n) -> {
            ScreenHandler screenHandler = Minecraft.INSTANCE.player.currentScreenHandler;
            NbtCompound nbt = p.getNbt();
            if (screenHandler == null || screenHandler.syncId != nbt.getInt("syncId")) {
                return;
            }
            screenHandler.glassguis_setProperty(nbt.getInt("propertyId"), nbt.getInt("value"));
        }, true, false);

        registerGlassPacket("glassguis:short", (p, n) -> {
            ScreenHandler screenHandler = Minecraft.INSTANCE.player.currentScreenHandler;
            NbtCompound nbt = p.getNbt();
            if (screenHandler == null || screenHandler.syncId != nbt.getInt("syncId")) {
                return;
            }
            screenHandler.glassguis_setProperty(nbt.getInt("propertyId"), nbt.getShort("value"));
        }, true, false);

        registerGlassPacket("glassguis:long", (p, n) -> {
            ScreenHandler screenHandler = Minecraft.INSTANCE.player.currentScreenHandler;
            NbtCompound nbt = p.getNbt();
            if (screenHandler == null || screenHandler.syncId != nbt.getInt("syncId")) {
                return;
            }
            screenHandler.glassguis_setProperty(nbt.getInt("propertyId"), nbt.getLong("value"));
        }, true, false);

        registerGlassPacket("glassguis:float", (p, n) -> {
            ScreenHandler screenHandler = Minecraft.INSTANCE.player.currentScreenHandler;
            NbtCompound nbt = p.getNbt();
            if (screenHandler == null || screenHandler.syncId != nbt.getInt("syncId")) {
                return;
            }
            screenHandler.glassguis_setProperty(nbt.getInt("propertyId"), nbt.getFloat("value"));
        }, true, false);

        registerGlassPacket("glassguis:double", (p, n) -> {
            ScreenHandler screenHandler = Minecraft.INSTANCE.player.currentScreenHandler;
            NbtCompound nbt = p.getNbt();
            if (screenHandler == null || screenHandler.syncId != nbt.getInt("syncId")) {
                return;
            }
            screenHandler.glassguis_setProperty(nbt.getInt("propertyId"), nbt.getDouble("value"));
        }, true, false);

        registerGlassPacket("glassguis:boolean", (p, n) -> {
            ScreenHandler screenHandler = Minecraft.INSTANCE.player.currentScreenHandler;
            NbtCompound nbt = p.getNbt();
            if (screenHandler == null || screenHandler.syncId != nbt.getInt("syncId")) {
                return;
            }
            screenHandler.glassguis_setProperty(nbt.getInt("propertyId"), nbt.getBoolean("value"));
        }, true, false);
    }
}
