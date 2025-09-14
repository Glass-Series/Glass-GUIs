package net.glasslauncher.mods.glassguis

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.network.NetworkHandler
import net.minecraft.network.packet.Packet
import java.io.DataInputStream
import java.io.DataOutputStream

class ScreenHandlerPropertyUpdateLongS2CPacket() : Packet() {
    var syncId: Int? = null
    var propertyId: Int? = null
    var long: Long? = null

    constructor(syncId: Int, propertyId: Int, long: Long) : this() {
        this.syncId = syncId
        this.propertyId = propertyId
        this.long = long
    }

    override fun read(stream: DataInputStream) {
        syncId = stream.readInt()
        propertyId = stream.readInt()
        long = stream.readLong()
    }

    override fun write(stream: DataOutputStream) {
        stream.writeInt(syncId!!)
        stream.writeInt(propertyId!!)
        stream.writeLong(long!!)
    }

    override fun apply(networkHandler: NetworkHandler) {
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) {
            return
        }

        val player = (FabricLoader.getInstance().gameInstance as Minecraft).player

        if (player.currentScreenHandler != null && player.currentScreenHandler is ExtendedScreenHandler && player.currentScreenHandler.syncId == syncId) {
            (player.currentScreenHandler as ExtendedScreenHandler).setProperty(propertyId!!, long!!)
        }
    }

    override fun size(): Int {
        return 16
    }
}