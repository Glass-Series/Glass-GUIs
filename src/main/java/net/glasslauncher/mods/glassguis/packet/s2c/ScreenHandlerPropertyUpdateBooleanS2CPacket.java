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

public class ScreenHandlerPropertyUpdateBooleanS2CPacket extends Packet implements ManagedPacket<ScreenHandlerPropertyUpdateBooleanS2CPacket> {
    private static final PacketType<ScreenHandlerPropertyUpdateBooleanS2CPacket> TYPE = PacketType.builder(true, false, ScreenHandlerPropertyUpdateBooleanS2CPacket::new).build();
    private int syncId;
    private int propertyId;
    private boolean booleanValue;

    public ScreenHandlerPropertyUpdateBooleanS2CPacket() {
    }

    public ScreenHandlerPropertyUpdateBooleanS2CPacket(int syncId, int propertyId, boolean booleanValue) {
        this.syncId = syncId;
        this.propertyId = propertyId;
        this.booleanValue = booleanValue;
    }

    @Override
    @SneakyThrows
    public void read(DataInputStream stream) {
        this.syncId = stream.readInt();
        this.propertyId = stream.readInt();
        this.booleanValue = stream.readBoolean();
    }

    @Override
    @SneakyThrows
    public void write(DataOutputStream stream) {
        stream.writeInt(syncId);
        stream.writeInt(propertyId);
        stream.writeBoolean(booleanValue);
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
            minecraft.player.currentScreenHandler.glassguis_setProperty(propertyId, booleanValue);
        }
    }

    @Override
    public int size() {
        return 12;
    }

    @Override
    public @NotNull PacketType<ScreenHandlerPropertyUpdateBooleanS2CPacket> getType() {
        return TYPE;
    }
}
