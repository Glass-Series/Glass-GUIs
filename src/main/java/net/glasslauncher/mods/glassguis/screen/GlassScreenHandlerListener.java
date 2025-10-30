package net.glasslauncher.mods.glassguis.screen;

import net.modificationstation.stationapi.api.util.Util;

public interface GlassScreenHandlerListener {
    default void glassguis_onPropertyUpdate(int propertyId, long longValue) { Util.assertImpl(); }
    default void glassguis_onPropertyUpdate(int propertyId, boolean booleanValue) { Util.assertImpl(); }
    default void glassguis_onPropertyUpdate(int propertyId, float floatValue) { Util.assertImpl(); }
    default void glassguis_onPropertyUpdate(int propertyId, double doubleValue) { Util.assertImpl(); }
    default void glassguis_onPropertyUpdate(int propertyId, short shortValue) { Util.assertImpl(); }
    default void glassguis_onPropertyUpdate(int propertyId, int intValue) { Util.assertImpl(); }
}
