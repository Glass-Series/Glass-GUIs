package net.glasslauncher.mods.glassguis.events.init;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;

public class GlassGUIs {
    public static final Cache<String, int[]> IMAGE_SIZE_CACHE = Caffeine.newBuilder().softValues().build();
//    public static @NotNull Logger LOGGER;
//
//    static  {
//        LOGGER = LogManager.getLogger("GlassGUIs");
//        Configurator.setLevel(LOGGER, Level.INFO);
//    }
}
