package net.glasslauncher.mods.glassguis.screen.widget;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.glassguis.screen.widget.element.WidgetElement;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public abstract class GlassBaseWidget extends DrawContext implements GlassWidget {
    @Getter
    protected int width;
    @Getter
    protected int height;
    @Getter
    protected int x;
    @Getter
    protected int y;
    @Getter
    protected Rectangle bounds;
    @Getter @Setter
    protected boolean enabled = true;
    @Getter @Setter
    protected boolean visible = true;
    @Getter
    protected boolean hovered;

    protected List<WidgetElement<?>> elements = new ArrayList<>();

    public void addElement(WidgetElement<?> element) {
        elements.add(element);
    }

    public void setElement(int index, WidgetElement<?> element) {
        elements.set(index, element);
    }

    public GlassBaseWidget(int x, int y, int width, int height) {
        setBounds(new Rectangle(x, y, width, height));
    }

    @Override
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
        this.x = bounds.getX();
        this.y = bounds.getY();
        this.width = bounds.getWidth();
        this.height = bounds.getHeight();
    }

    protected abstract void renderBackground(int mouseX, int mouseY, float delta);

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        hovered = bounds.contains(mouseX, mouseY);
        if (!visible) {
            return;
        }

        renderBackground(mouseX, mouseY, delta);
        renderElements(mouseX, mouseY, delta);
    }

    @Override
    public void renderElements(int mouseX, int mouseY, float delta) {
        for (WidgetElement<?> element : elements) {
            WidgetElement.OffsetDimension dimension = element.computeAlignment(width, height);
            element.render(x + dimension.x(), y + dimension.y(), element.getWidth(), element.getHeight());
        }
    }
}
