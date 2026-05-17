package net.glasslauncher.mods.glassguistest.events.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.mods.glassguis.DrawDirection;
import net.glasslauncher.mods.glassguis.ImageUtil;
import net.glasslauncher.mods.glassguis.screen.SyncedTileEntityValue;
import net.glasslauncher.mods.glassguis.screen.widget.GlassButton;
import net.glasslauncher.mods.glassguis.screen.widget.GlassEntryListWidget;
import net.glasslauncher.mods.glassguis.screen.widget.ImageWidget;
import net.glasslauncher.mods.glassguis.screen.widget.element.TextElement;
import net.glasslauncher.mods.glassguis.screen.widget.element.WidgetElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TestBlockGUI extends HandledScreen {
//    GlassEntryListWidget listWidget;
    ImageWidget image;
    GlassButton button;

    public TestBlockGUI(PlayerInventory inventory, TestBlockEntity container) {
        super(new TestBlockHandler(inventory, container));
//        listWidget = new Widget(Minecraft.INSTANCE, 200, 200, 10, 10, 10);
//        listWidget.setDrawSelectedBox(true);
//        glassguis_addWidget(listWidget);
        ImageUtil.Image img = ImageUtil.Image.of("/assets/glassguis_test/stationapi/textures/item/acacia.png");
        image = new ImageWidget(20, 50, 32, 32, img);
        glassguis_addWidget(image);
        image.addElement(new TextElement("Oh yeah, I can write on this", image.getWidth(), 0));
        glassguis_addWidget(new ImageWidget(20, 90, 8, 8, img));
        glassguis_addWidget(new ImageWidget(20, 100, 32, 16, img));
        glassguis_addWidget(new ImageWidget(20, 120, 24, 32, img));
        button = new GlassButton(20, 20, "Wait, this works?", () -> {});
        button.addImage("/assets/glassguis_test/stationapi/textures/item/acacia.png", WidgetElement.Alignment.LEFT);
        button.addText("What?", WidgetElement.Alignment.LEFT);
        button.addText("Huh???", WidgetElement.Alignment.RIGHT);
        glassguis_addWidget(button);
    }

    @Override
    public void init() {
//        listWidget.setBounds(new Rectangle(100, 100, 200, 200));
        super.init();
    }

    @Override
    public void drawBackground(float delta) {
        glassguis_renderBackground();
        glassguis_drawSlots();

        int x = ((width - backgroundWidth) / 2);
        int y = ((height - backgroundHeight) / 2);

        fill(x + 10, y + 30, x + 10 + 16, y + 30 + 16, -1);
        fill(x + 10, y + 50, x + 10 + 16, y + 50 + 16, -1);
        fill(x + 10, y + 70, x + 10 + 16, y + 70 + 16, -1);
        fill(x + 10, y + 90, x + 10 + 16, y + 90 + 16, -1);

        glassguis_drawImagePercentage("/assets/glassguis_test/stationapi/textures/item/acacia.png", 10, 30, 0.8f, DrawDirection.UP);
        glassguis_drawImagePercentage("/assets/glassguis_test/stationapi/textures/item/acacia.png", 10, 50, 0.8f, DrawDirection.DOWN);
        glassguis_drawImagePercentage("/assets/glassguis_test/stationapi/textures/item/acacia.png", 10, 70, 0.8f, DrawDirection.LEFT);
        glassguis_drawImagePercentage("/assets/glassguis_test/stationapi/textures/item/acacia.png", 10, 90, 0.8f, DrawDirection.RIGHT);

        drawTextWithShadow(textRenderer, String.valueOf(((TestBlockHandler) container).testBlockEntity.getTest()), x + 50, y + 50, -1);
    }

    private class Widget extends GlassEntryListWidget {
        List<String> entries = new ArrayList<>() {{
            for (int i = 0; i < 20; i++) {
                add("Test " + i);
            }
        }};
        int selIndex = -1;
        boolean dragging = false;
        boolean resizing = false;

        public Widget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
            super(minecraft, (TestBlockGUI.this.width / 2) - (width / 2), 30, width, height, top, bottom, itemHeight);
        }

        @Override
        public void onMouseDown(int mouseX, int mouseY, int button) {
            super.onMouseDown(mouseX, mouseY, button);
            if (button == 0 && mouseX < x + 5 && mouseY < y + 5) {
                dragging = true;
            }
            else if (button == 0 && mouseX > x + width - 5 && mouseY > y + height - 5) {
                resizing = true;
            }
        }

        @Override
        public void onMouseUp(int mouseX, int mouseY, int button) {
            super.onMouseUp(mouseX, mouseY, button);
            if (button == 0) {
                dragging = false;
                resizing = false;
            }
        }

        @Override
        public void render(int mouseX, int mouseY, float frameDelta) {
            if (dragging) {
                setBounds(new Rectangle(mouseX, mouseY, width, height));
            }
            if (resizing) {
                setBounds(new Rectangle(x, y, mouseX - x, mouseY - y));
            }
            super.render(mouseX, mouseY, frameDelta);
        }

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
        protected void renderEntry(int index, int x, int y, int width, int height, Tessellator tessellator) {
            Minecraft.INSTANCE.textRenderer.drawWithShadow(entries.get(index), x, y, -1);
        }

        @Override
        public void renderElements(int mouseX, int mouseY, float delta) {

        }
    };
}