package net.glasslauncher.mods.glassguis.compat;

import net.glasslauncher.mods.glassguis.screen.GlassScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;

public class StationAPICompat {

    public static void drawSprite(Slot slot, int x, int y, GlassScreen screen) {
        Sprite sprite = Atlases.getGuiItems().getTexture(slot.getBackgroundTextureId()).getSprite();
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(sprite.getAtlasId());
        atlas.bindTexture();
        screen.glassguis_drawTexture(x + slot.x, y + slot.y, sprite.getContents().getWidth(), sprite.getContents().getWidth());
    }

    public static void drawSprite(int x, int y, int width, int height, Sprite sprite) {
        double startU = sprite.getMinU();
        double startV = sprite.getMinV();
        double u = sprite.getMaxU();
        double v = sprite.getMaxV();
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        tessellator.vertex((x + 0D), (y + height), 0.0, startU, v); // bl
        tessellator.vertex((x + width), (y + height), 0.0, u, v); // br
        tessellator.vertex((x + width), (y + 0D), 0.0, u, startV); // tr
        tessellator.vertex((x + 0D), (y + 0D), 0.0, startU, startV); // tl
        tessellator.draw();
    }
}
