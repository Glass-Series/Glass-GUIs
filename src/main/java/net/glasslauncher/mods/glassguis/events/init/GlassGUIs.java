package net.glasslauncher.mods.glassguis.events.init;

import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

public class GlassGUIs {
    public static @NotNull Logger LOGGER;

    static  {
        LOGGER = LogManager.getLogger("GlassGUIs");
        Configurator.setLevel(LOGGER, Level.INFO);
    }
}
