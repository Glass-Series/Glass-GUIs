package net.glasslauncher.mods.glassguistest.events.init;

import net.glasslauncher.mods.glassguis.screen.slot.BigHitboxSlot;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;

import static net.glasslauncher.mods.glassguis.compat.StationAPICompat.drawSprite;

public class BatterySlot extends BigHitboxSlot {

    public BatterySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean renderExtras() {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(GlassGUIsTestClient.sprite.getSprite().getAtlasId());
        atlas.bindTexture();
        drawSprite(x, y, GlassGUIsTestClient.sprite.getWidth(), GlassGUIsTestClient.sprite.getHeight(), GlassGUIsTestClient.sprite.getSprite());
        return true;
    }
}