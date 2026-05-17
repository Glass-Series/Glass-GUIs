package net.glasslauncher.mods.glassguis.screen.widget.element;

import net.glasslauncher.mods.glassguis.ImageUtil;

public class ImageElement extends WidgetElement<ImageUtil.Image> {
    public static final WidgetElement.Renderer<ImageUtil.Image> IMAGE_RENDERER = (x, y, w, h, e) -> e.getContent().draw(x, y, w, h, true);

    public ImageElement(ImageUtil.Image content, int width, int height) {
        super(content, width, height, IMAGE_RENDERER);
    }

    public ImageElement(ImageUtil.Image content, int width, int height, Alignment alignment) {
        super(content, width, height, alignment, IMAGE_RENDERER);
    }
}
