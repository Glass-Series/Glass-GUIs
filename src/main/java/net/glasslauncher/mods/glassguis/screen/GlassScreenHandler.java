package net.glasslauncher.mods.glassguis.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.util.Util;

import java.lang.reflect.Field;

public interface GlassScreenHandler {

    @Environment(EnvType.CLIENT)
    default Field glassguis_getSettableField(int propertyId, Class<?> type) { Util.assertImpl(); return null; }

    @Environment(EnvType.CLIENT)
    default void glassguis_setProperty(int propertyId, long longValue) {
        Field field = glassguis_getSettableField(propertyId, long.class);
        if (field != null) {
            try {
                field.setLong(((AutoSyncingScreenHandler) this).getBlockEntity(), longValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    default void glassguis_setProperty(int propertyId, boolean booleanValue) {
        Field field = glassguis_getSettableField(propertyId, boolean.class);
        if (field != null) {
            try {
                field.setBoolean(((AutoSyncingScreenHandler) this).getBlockEntity(), booleanValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    default void glassguis_setProperty(int propertyId, float floatValue) {
        Field field = glassguis_getSettableField(propertyId, float.class);
        if (field != null) {
            try {
                field.setFloat(((AutoSyncingScreenHandler) this).getBlockEntity(), floatValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    default void glassguis_setProperty(int propertyId, double doubleValue) {
        Field field = glassguis_getSettableField(propertyId, double.class);
        if (field != null) {
            try {
                field.setDouble(((AutoSyncingScreenHandler) this).getBlockEntity(), doubleValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    default void glassguis_setProperty(int propertyId, short shortValue) {
        Field field = glassguis_getSettableField(propertyId, short.class);
        if (field != null) {
            try {
                field.setShort(((AutoSyncingScreenHandler) this).getBlockEntity(), shortValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    default void glassguis_setupPlayerInventory(int x, int y, PlayerInventory playerInventory) { Util.assertImpl(); }
}
