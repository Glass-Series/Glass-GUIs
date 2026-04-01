package net.glasslauncher.mods.glassguistest.events.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

import java.lang.invoke.MethodHandles;

public class GlassGUIsTestClient {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    public static Atlas.Sprite sprite = null;

    @EventListener
    public void gui(GuiHandlerRegistryEvent event) {
        event.register(GlassGUIsTest.NAMESPACE.id("testgui"), new GuiHandler((player, inventory, packet) -> new TestBlockGUI(player.inventory, (TestBlockEntity) inventory), TestBlockEntity::new));
    }

    @EventListener
    public void tex(TextureRegisterEvent event) {
        sprite = Atlases.getGuiItems().addTexture(GlassGUIsTest.NAMESPACE.id("item/battery_slot"));
    }
}
