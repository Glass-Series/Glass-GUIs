package net.glasslauncher.mods.glassguis.screen.widget.element;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;

public class TextElement extends WidgetElement<String> {
    public static final Renderer<String> TEXT_RENDERER = (x, y, maxLineWidth, lineHeight, element) -> {
        int additive = element.getColor().getRGB() & -16777216;
        int shadowColor = (element.getColor().getRGB() & 16579836) >> 2;
        shadowColor += additive;

        AMITextRenderer.INSTANCE.drawSplit(element.getContent(), x + 1, y + 1, maxLineWidth, shadowColor);
        AMITextRenderer.INSTANCE.drawSplit(element.getContent(), x, y, maxLineWidth, element.getColor().getRGB());
    };

    public TextElement(String content, int width, int height) {
        super(content, width, height, TEXT_RENDERER);
    }

    public TextElement(String content, int width, int height, Alignment alignment) {
        super(content, width, height, alignment, TEXT_RENDERER);
    }
}
