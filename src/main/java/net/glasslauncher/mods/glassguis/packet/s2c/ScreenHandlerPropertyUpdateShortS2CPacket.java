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

public class ScreenHandlerPropertyUpdateShortS2CPacket extends Packet implements ManagedPacket<ScreenHandlerPropertyUpdateShortS2CPacket> {
    private static final PacketType<ScreenHandlerPropertyUpdateShortS2CPacket> TYPE = PacketType.builder(true, false, ScreenHandlerPropertyUpdateShortS2CPacket::new).build();
    private int syncId;
    private int propertyId;
    private short shortValue;

    public ScreenHandlerPropertyUpdateShortS2CPacket() {
    }

    public ScreenHandlerPropertyUpdateShortS2CPacket(int syncId, int propertyId, short shortValue) {
        this.syncId = syncId;
        this.propertyId = propertyId;
        this.shortValue = shortValue;
    }

    @Override
    @SneakyThrows
    public void read(DataInputStream stream) {
        this.syncId = stream.readInt();
        this.propertyId = stream.readInt();
        this.shortValue = stream.readShort();
    }

    @Override
    @SneakyThrows
    public void write(DataOutputStream stream) {
        stream.writeInt(syncId);
        stream.writeInt(propertyId);
        stream.writeShort(shortValue);
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
            minecraft.player.currentScreenHandler.glassguis_setProperty(propertyId, shortValue);
        }
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public @NotNull PacketType<ScreenHandlerPropertyUpdateShortS2CPacket> getType() {
        return TYPE;
    }
}
