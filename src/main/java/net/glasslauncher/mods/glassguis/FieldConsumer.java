package net.glasslauncher.mods.glassguis;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldConsumer {
    void apply(ScreenHandler screenHandler, Field field, ServerPlayerEntity serverPlayerEntity, int index) throws IllegalAccessException;
}
