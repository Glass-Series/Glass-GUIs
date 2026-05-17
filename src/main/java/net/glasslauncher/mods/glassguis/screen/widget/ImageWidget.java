package net.glasslauncher.mods.glassguis.screen.widget;

import net.glasslauncher.mods.alwaysmoreitems.gui.AMIDrawContext;
import net.glasslauncher.mods.glassguis.ImageUtil;
import net.glasslauncher.mods.glassguis.screen.widget.element.ImageElement;

public class ImageWidget extends GlassBaseWidget {
    public ImageWidget(int x, int y, int width, int height, ImageUtil.Image image) {
        super(x, y, width, height);
        addElement(new ImageElement(image, width, height));
    }

    @Override
    protected void renderBackground(int mouseX, int mouseY, float delta) {
    }

    @Override
    public void onMouseScroll(int mouseX, int mouseY, int wheelDelta) {

    }

    @Override
    public void onMouseDown(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onMouseUp(int mouseX, int mouseY, int button) {

    }
}
