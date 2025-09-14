package net.glasslauncher.mods.glassguistest.events.init

import net.minecraft.block.FurnaceBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtInt
import net.modificationstation.stationapi.api.recipe.FuelRegistry
import net.modificationstation.stationapi.api.state.property.Properties
import kotlin.collections.get

open class GuiTestBlockEntity : BlockEntity(), Inventory {

    protected var slots: Array<ItemStack?> = arrayOfNulls(3)

    override fun writeNbt(tag: NbtCompound) {
        super.writeNbt(tag)
        for (i in slots.indices) {
            if (slots[i] != null) {
                val item = NbtCompound()
                slots[i]!!.writeNbt(item)
                tag.put("item$i", item)
            }
        }
    }

    override fun readNbt(tag: NbtCompound) {
        super.readNbt(tag)
        for (i in slots.indices) {
            if (tag.contains("item$i")) {
                slots[i] = ItemStack(tag.getCompound("item$i"))
            }
        }
    }

    override fun size(): Int {
        return 2
    }

    override fun getStack(slot: Int): ItemStack? {
        return slots[slot]
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack? {
        var stack: ItemStack = getStack(slot) ?: return null

        if (stack.count == amount) {
            slots[slot] = null
            return stack
        }
        stack.count -= amount
        stack = stack.copy()
        stack.count = amount
        return stack
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        slots[slot] = stack
    }

    override fun getName(): String {
        return "Generator"
    }

    override fun getMaxCountPerStack(): Int {
        return 64
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return player.getSquaredDistance(x + 0.5, y + 0.5, z + 0.5) <= 64
    }
}