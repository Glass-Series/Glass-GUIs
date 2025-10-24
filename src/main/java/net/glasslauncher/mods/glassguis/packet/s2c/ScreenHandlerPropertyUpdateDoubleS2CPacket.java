package net.glasslauncher.mods.glassguis.packet.s2c;

import lombok.SneakyThrows;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ScreenHandlerPropertyUpdateDoubleS2CPacket extends Packet implements ManagedPacket<ScreenHandlerPropertyUpdateDoubleS2CPacket> {
    private static final PacketType<ScreenHandlerPropertyUpdateDoubleS2CPacket> TYPE = PacketType.builder(true, false, ScreenHandlerPropertyUpdateDoubleS2CPacket::new).build();
    private int syncId;
    private int propertyId;
    private double doubleValue;

    public ScreenHandlerPropertyUpdateDoubleS2CPacket() {
    }

    public ScreenHandlerPropertyUpdateDoubleS2CPacket(int syncId, int propertyId, double doubleValue) {
        this.syncId = syncId;
        this.propertyId = propertyId;
        this.doubleValue = doubleValue;
    }

    @Override
    @SneakyThrows
    public void read(DataInputStream stream) {
        this.syncId = stream.readInt();
        this.propertyId = stream.readInt();
        this.doubleValue = stream.readDouble();
    }

    @Override
    @SneakyThrows
    public void write(DataOutputStream stream) {
        stream.writeInt(syncId);
        stream.writeInt(propertyId);
        stream.writeDouble(doubleValue);
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }

        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (
                minecraft.player != null &&
                minecraft.player.currentScreenHandler != null &&
                minecraft.player.currentScreenHandler.syncId == syncId
        ) {
            minecraft.player.currentScreenHandler.glassguis_setProperty(propertyId, doubleValue);
        }
    }

    @Override
    public int size() {
        return 24;
    }

    @Override
    public @NotNull PacketType<ScreenHandlerPropertyUpdateDoubleS2CPacket> getType() {
        return TYPE;
    }
}
