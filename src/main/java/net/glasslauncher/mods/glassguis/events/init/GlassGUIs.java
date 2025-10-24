package net.glasslauncher.mods.glassguis.events.init;

import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.RecipesGui;
import net.glasslauncher.mods.alwaysmoreitems.util.AlwaysMoreItems;
import net.glasslauncher.mods.alwaysmoreitems.util.RecipeGuiLogic;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

public class GlassGUIs {
    static  {
        EntrypointManager.registerLookup(MethodHandles.lookup());
        OverlayScreen.INSTANCE.recipesGui.isActive();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static @NotNull Namespace NAMESPACE = Namespace.resolve();
    public static @NotNull Logger LOGGER = NAMESPACE.getLogger("GlassGUIs");
}
