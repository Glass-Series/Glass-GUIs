package net.glasslauncher.mods.glassguis.screen.widget.element;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMIDrawContext;

import java.awt.*;

public class WidgetElement<T> {
    @Getter
    private final T content;
    @Getter
    private final int width;
    @Getter
    private final int height;
    private final Renderer<T> renderer;
    @Getter
    private final Alignment alignment;
    @Getter @Setter
    private Color color = Color.WHITE;

    public WidgetElement(T content, int width, int height, Renderer<T> renderer) {
        this.content = content;
        this.width = width;
        this.height = height;
        this.alignment = Alignment.TOP_LEFT;
        this.renderer = renderer;
    }

    @FunctionalInterface
    public interface Renderer<T> {
        void render(int x, int y, int maxWidth, int lineHeight, WidgetElement<T> element);
    }

    public WidgetElement(T content, int width, int height, Alignment alignment, Renderer<T> renderer) {
        this.content = content;
        this.width = width;
        this.height = height;
        this.alignment = alignment;
        this.renderer = renderer;
    }

    public void render(int x, int y, int width, int height) {
        OffsetDimension dimension = computeAlignment(width, height);
        renderer.render(x + dimension.x, y + dimension.y, width, height, this);
//        AMIDrawContext.INSTANCE.fill(x + dimension.x, y + dimension.y, x + dimension.x + width, y + dimension.y + height, -1);
    }

    public OffsetDimension computeAlignment(int maxLineWidth, int lineHeight) {
        return alignment.compute(width, height, maxLineWidth, lineHeight);
    }

    private static final OffsetDimension ZERO_DIMENSION = new OffsetDimension(0, 0);

    public record OffsetDimension(int x, int y) {}

    @FunctionalInterface
    public interface Alignment {
        Alignment TOP_LEFT = ((width, height, maxLineWidth, lineHeight) -> ZERO_DIMENSION);
        Alignment TOP_RIGHT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(maxLineWidth - width, 0));
        Alignment TOP_MIDDLE = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension((maxLineWidth / 2) - (width / 2), 0));
        Alignment BOTTOM_LEFT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(0, lineHeight - height));
        Alignment BOTTOM_RIGHT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(maxLineWidth - width, lineHeight - height));
        Alignment BOTTOM_MIDDLE = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension((maxLineWidth / 2) - (width / 2), lineHeight - height));
        Alignment LEFT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(0, (lineHeight / 2) - (height / 2)));
        Alignment RIGHT = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension(maxLineWidth - width, (lineHeight / 2) - (height / 2)));
        Alignment CENTER = ((width, height, maxLineWidth, lineHeight) -> new OffsetDimension((maxLineWidth / 2) - (width / 2), (lineHeight / 2) - (height / 2)));

        OffsetDimension compute(int width, int height, int maxLineWidth, int lineHeight);
    }
}