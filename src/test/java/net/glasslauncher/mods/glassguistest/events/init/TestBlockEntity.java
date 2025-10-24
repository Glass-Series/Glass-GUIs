package net.glasslauncher.mods.glassguistest.events.init;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class TestBlockEntity extends BlockEntity implements Inventory {
    protected ItemStack[] slots = new ItemStack[3];

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                NbtCompound item = new NbtCompound();
                slots[i].writeNbt(item);
                tag.put("item" + i, item);
            }
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        for (int i = 0; i < slots.length; i++) {
            if (tag.contains("item" + i)) {
                slots[i] = new ItemStack(tag.getCompound("item" + i));
            }
        }
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slots[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        var stack = getStack(slot);

        if (stack.count == amount) {
            slots[slot] = null;
            return stack;
        }
        stack.count -= amount;
        stack = stack.copy();
        stack.count = amount;
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        slots[slot] = stack;
    }

    @Override
    public String getName() {
        return "Generator";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.getSquaredDistance(x + 0.5, y + 0.5, z + 0.5) <= 64;
    }
}