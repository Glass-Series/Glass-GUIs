package net.glasslauncher.mods.glassguis.events.init

import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager
import net.modificationstation.stationapi.api.util.Namespace
import org.apache.logging.log4j.Logger
import java.lang.invoke.MethodHandles

class GlassGUIs {
    companion object {
        @Suppress("UnstableApiUsage")
        val NAMESPACE: Namespace = Namespace.resolve()
        val LOGGER: Logger = NAMESPACE.getLogger("GlassGUIs")

        init {
            EntrypointManager.registerLookup(MethodHandles.lookup())
        }
    }
}