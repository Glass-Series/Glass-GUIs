package net.glasslauncher.mods.glassguis.mixin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.glasslauncher.mods.glassguis.ClassCacheEntry;
import net.glasslauncher.mods.glassguis.FieldConsumer;
import net.glasslauncher.mods.glassguis.GlassScreenHandler;
import net.glasslauncher.mods.glassguis.SyncedScreenHandlerValue;
import net.mine_diver.spasm.impl.util.TriFunction;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements GlassScreenHandler {
    @Unique
    private static final Cache<Class<?>, Field[]> CLASS_TO_FIELD_CACHE = Caffeine.newBuilder().build();
    @Unique
    private static final Map<Class<?>, FieldConsumer> SUPPORTED_FIELDS = new HashMap<>() {{
        put(Long.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getLong(s)));
        put(Double.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getDouble(s)));
        put(Integer.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getInt(s)));
        put(Short.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getShort(s)));
        put(Boolean.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getBoolean(s)));
        put(Float.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getFloat(s)));

    }};

    @Shadow public abstract void addSlot(Slot slot);

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

    @Inject(method = "addListener", at = @At("RETURN"))
    private void iHateHowThisIsDone(ScreenHandlerListener par1, CallbackInfo ci) {
        if (par1 instanceof ServerPlayerEntity player) { // Currently ScreenHandlerListener is only implemented by ServerPlayerEntity... so we should be fine.
            var fields = CLASS_TO_FIELD_CACHE.get(getClass(), key -> getFieldsRecursive(key).toArray(new Field[0]));
            if (fields.length == 0) {
                return;
            }
            try {
                //noinspection ConstantValue Fuck off intellij, this is a mixin
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    var entry = SUPPORTED_FIELDS.get(field.getType());
                    if (entry == null) {
                        throw new RuntimeException("Unsupported field type " + field.getType() + " on field " + field.getDeclaringClass() + "." + field.getName() + "!");
                    }
                    //noinspection DataFlowIssue ditto, you fuck
                    entry.apply((ScreenHandler) (Object) this, field, player, i);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Unique
    private List<Field> getFieldsRecursive(Class<?> c) {
        if (c == null) {
            return Collections.emptyList();
        }

        var fields = new ArrayList<>(getFieldsRecursive(c.getSuperclass()));
        for (Field field : c.getDeclaredFields()) {
            if (field.getAnnotation(SyncedScreenHandlerValue.class) != null) {
                fields.add(field);
            }
        }
        return fields;
    }
}
