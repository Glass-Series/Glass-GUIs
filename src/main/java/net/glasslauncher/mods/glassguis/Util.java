package net.glasslauncher.mods.glassguis;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.glasslauncher.mods.glassguis.screen.SyncedScreenHandlerValue;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.*;

public class Util {
    private static final Cache<Class<?>, Field[]> CLASS_TO_FIELD_CACHE = Caffeine.newBuilder().build();
    private static final Map<Class<?>, FieldConsumer> SUPPORTED_FIELDS = new HashMap<>() {{
        put(Long.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getLong(s)));
        put(Double.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getDouble(s)));
        put(Integer.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getInt(s)));
        put(Short.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getShort(s)));
        put(Boolean.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getBoolean(s)));
        put(Float.TYPE, (s, f, p, i) -> s.glassguis_setProperty(i, f.getFloat(s)));

    }};

    public static void iHateHowThisIsDone(ScreenHandlerListener par1, ScreenHandler source) {
        if (par1 instanceof ServerPlayerEntity player) { // Currently ScreenHandlerListener is only implemented by ServerPlayerEntity... so we should be fine.
            var fields = CLASS_TO_FIELD_CACHE.get(Util.class, key -> getFieldsRecursive(key).toArray(new Field[0]));
            if (fields.length == 0) {
                return;
            }
            try {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    var entry = SUPPORTED_FIELDS.get(field.getType());
                    if (entry == null) {
                        throw new RuntimeException("Unsupported field type " + field.getType() + " on field " + field.getDeclaringClass() + "." + field.getName() + "!");
                    }
                    entry.apply(source, field, player, i);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static List<Field> getFieldsRecursive(Class<?> c) {
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
