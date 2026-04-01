package net.glasslauncher.mods.glassguis;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.glasslauncher.mods.glassguis.screen.AutoSyncingScreenHandler;
import net.glasslauncher.mods.glassguis.screen.ServerSyncedField;
import net.glasslauncher.mods.networking.GlassNetworking;
import net.glasslauncher.mods.networking.GlassPacket;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;

import java.lang.reflect.Field;
import java.util.*;

public class GGUIUtil {
    private static final Cache<Class<?>, Field[]> CLASS_TO_FIELD_CACHE = Caffeine.newBuilder().build();
    private static final Set<Class<?>> SUPPORTED_FIELDS = Set.of(
            long.class,
            double.class,
            int.class,
            short.class,
            boolean.class,
            float.class
    );

    public static Field[] getSyncedFields(Class<?> cls) {
        return CLASS_TO_FIELD_CACHE.getIfPresent(cls);
    }

    public static void checkAndCacheFields(BlockEntity source) {
        CLASS_TO_FIELD_CACHE.get(source.getClass(), key -> getServerSyncedFieldsRecursively(key).toArray(new Field[0]));
    }

    private static List<Field> getServerSyncedFieldsRecursively(Class<?> cls) {
        List<Field> foundFields = new ArrayList<>();

        while (cls != null) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (!field.isAnnotationPresent(ServerSyncedField.class)) {
                    continue;
                }
                if (!SUPPORTED_FIELDS.contains(field.getType())) {
                    throw new RuntimeException("Unsupported field type " + field.getType() + " on field " + field.getDeclaringClass() + ":" + field.getName() + "!");
                }
                foundFields.add(field);
                field.setAccessible(true);
            }
            cls = cls.getSuperclass();
        }

        return foundFields;
    }

    public static void handleAutoSend(PlayerEntity player, ScreenHandler screenHandler) {
        BlockEntity blockEntity = ((AutoSyncingScreenHandler) screenHandler).getBlockEntity();
        Field[] fields = GGUIUtil.getSyncedFields(blockEntity.getClass());
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Class<?> type = field.getType();

                NbtCompound data = new NbtCompound();
                data.putInt("syncId", screenHandler.syncId);
                data.putInt("propertyId", i);

                String packetName;

                if (type == int.class) {
                    data.putInt("value", field.getInt(blockEntity));
                    packetName = "int";
                }
                else if (type == boolean.class) {
                    data.putBoolean("value", field.getBoolean(blockEntity));
                    packetName = "boolean";
                }
                else if (type == double.class) {
                    data.putDouble("value", field.getDouble(blockEntity));
                    packetName = "double";
                }
                else if (type == float.class) {
                    data.putFloat("value", field.getFloat(blockEntity));
                    packetName = "float";
                }
                else if (type == long.class) {
                    data.putLong("value", field.getLong(blockEntity));
                    packetName = "long";
                }
                else if (type == short.class) {
                    data.putShort("value", field.getShort(blockEntity));
                    packetName = "short";
                }
                else {
                    throw new RuntimeException("Invalid type!");
                }
                GlassNetworking.sendToPlayer(player, new GlassPacket("glassguis", packetName, data));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // This should never happen
        }
    }
}
