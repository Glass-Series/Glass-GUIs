package net.glasslauncher.mods.glassguis;

import com.google.gson.GsonBuilder;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.util.Util;

public interface GlassScreenHandler {
    default void glassguis_setProperty(int propertyId, long longValue) { Util.assertImpl(); }
    default void glassguis_setProperty(int propertyId, boolean longValue) { Util.assertImpl(); }
    default void glassguis_setProperty(int propertyId, float longValue) { Util.assertImpl(); }
    default void glassguis_setProperty(int propertyId, double longValue) { Util.assertImpl(); }
    default void glassguis_setProperty(int propertyId, short longValue) { Util.assertImpl(); }

    default void glassguis_setupPlayerInventory(int x, int y, PlayerInventory playerInventory) { Util.assertImpl(); }
}
