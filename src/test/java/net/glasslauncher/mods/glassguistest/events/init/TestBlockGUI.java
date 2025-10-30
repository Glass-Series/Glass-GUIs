package net.glasslauncher.mods.glassguistest.events.init;

import net.glasslauncher.mods.glassguis.screen.widget.GlassEntryListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class TestBlockGUI extends HandledScreen {
    GlassEntryListWidget widget = new GlassEntryListWidget(Minecraft.INSTANCE, 64, 64, 10, 10, 10) {
        @Override
        protected int getEntriesHeight() {
            return 10;
        }

        List<String> entries = new ArrayList<>();
        int selIndex = -1;

        @Override
        protected int getEntryCount() {
            return entries.size();
        }

        @Override
        protected void entryClicked(int index, boolean doubleClick) {
            selIndex = index;
        }

        @Override
        protected boolean isSelectedEntry(int index) {
            return index == selIndex;
        }

        @Override
        protected void renderBackground() {

        }

        @Override
        protected void renderEntry(int index, int x, int width, int y, int i, Tessellator tessellator) {
            textRenderer.drawWithShadow(entries.get(index), x, y, -1);
        }
    };

    public TestBlockGUI(PlayerInventory inventory, TestBlockEntity container) {
        super(new TestBlockHandler(inventory, container));
        widget.setDrawSelectedBox(true);
    }

    @Override
    public void drawBackground(float delta) {
        glassguis_renderBackground(this);
        glassguis_drawSlots(this);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        widget.render(mouseX, mouseY, delta);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        widget.onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        widget.onMouseReleased(mouseX, mouseY, button);
    }
}