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

public class ScreenHandlerPropertyUpdateLongS2CPacket extends Packet implements ManagedPacket<ScreenHandlerPropertyUpdateLongS2CPacket> {
    private static final PacketType<ScreenHandlerPropertyUpdateLongS2CPacket> TYPE = PacketType.<ScreenHandlerPropertyUpdateLongS2CPacket>builder(true, false, ScreenHandlerPropertyUpdateLongS2CPacket::new).build();
    private int syncId;
    private int propertyId;
    private long longValue;

    public ScreenHandlerPropertyUpdateLongS2CPacket() {
    }

    public ScreenHandlerPropertyUpdateLongS2CPacket(int syncId, int propertyId, long longValue) {
        this.syncId = syncId;
        this.propertyId = propertyId;
        this.longValue = longValue;
    }

    @Override
    @SneakyThrows
    public void read(DataInputStream stream) {
        this.syncId = stream.readInt();
        this.propertyId = stream.readInt();
        this.longValue = stream.readLong();
    }

    @Override
    @SneakyThrows
    public void write(DataOutputStream stream) {
        stream.writeInt(syncId);
        stream.writeInt(propertyId);
        stream.writeLong(longValue);
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
            minecraft.player.currentScreenHandler.glassguis_setProperty(propertyId, longValue);
        }
    }

    @Override
    public int size() {
        return 16;
    }

    @Override
    public @NotNull PacketType<ScreenHandlerPropertyUpdateLongS2CPacket> getType() {
        return TYPE;
    }
}
