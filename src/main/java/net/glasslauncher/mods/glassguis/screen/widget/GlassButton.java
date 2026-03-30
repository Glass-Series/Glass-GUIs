package net.glasslauncher.mods.glassguis.screen.widget;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.gcapi3.api.CharacterUtils;
import net.glasslauncher.mods.glassguis.ImageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlassButton extends DrawContext implements GlassWidget {
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    private int x;
    @Getter
    private int y;
    @Getter @Setter
    private boolean enabled = true;
    @Getter @Setter
    private boolean visible = true;
    @Getter @Setter
    private Runnable action;
    @Getter @Setter
    private int color = CharacterUtils.getIntFromColour(new Color(224, 224, 224));
    @Getter @Setter
    private int hoveredColor = CharacterUtils.getIntFromColour(new Color(255, 255, 160));
    @Getter @Setter
    private int disabledColor = CharacterUtils.getIntFromColour(new Color(255, 160, 160, 160));
    @Getter
    private boolean hovered;
    @Getter
    private Rectangle bounds;

    protected List<ButtonElement<?>> contents = new ArrayList<>();


    public GlassButton(int x, int y, String text, Runnable action) {
        this(x, y, 200, 20, text, action);
    }

    public GlassButton(int x, int y, String text, Alignment alignment, Runnable action) {
        this(x, y, 200, 20, text, alignment, action);
    }

    public GlassButton(int x, int y, int width, int height, String text, Runnable action) {
        this(x, y, text, Alignment.CENTER, action);
    }

    public GlassButton(int x, int y, int width, int height, String text, Alignment alignment, Runnable action) {
        this.action = action;
        setBounds(new Rectangle(x, y, width, height));
        addText(text, alignment);
    }

    public GlassButton(int x, int y, int width, int height, String image, String text, Runnable action) {
        this(x, y, width, height, text, Alignment.CENTER, action);
    }

    public GlassButton(int x, int y, int width, int height, String image, String text, Alignment alignment, Runnable action) {
        this.action = action;
        setBounds(new Rectangle(x, y, width, height));
        addImage(image);
        addText(text, alignment);
    }

    public void addText(String text) {
        addText(text, Alignment.CENTER);
    }

    public void addText(String text, Alignment alignment) {
        ButtonElement<String> element = new ButtonElement<>(text, Minecraft.INSTANCE.textRenderer.getWidth(text), alignment, (x, y, w) -> Minecraft.INSTANCE.textRenderer.drawWithShadow(text, x, y + 6, enabled ? hovered ? hoveredColor : color : disabledColor));
        addElement(element);
    }

    public void setText(int index, String text) {
        setText(index, text, Alignment.CENTER);
    }

    public void setText(int index, String text, Alignment alignment) {
        ButtonElement<String> element = new ButtonElement<>(text, Minecraft.INSTANCE.textRenderer.getWidth(text), alignment, (x, y, e) -> Minecraft.INSTANCE.textRenderer.drawWithShadow(e.content, x, y + 6, enabled ? hovered ? hoveredColor : color : disabledColor));
        setElement(index, element);
    }

    public void addImage(String image) {
        addImage(image, Alignment.CENTER);
    }

    public void addImage(String image, Alignment alignment) {
        ImageUtil.Image img = ImageUtil.Image.of(image);
        addElement(new ButtonElement<>(img, img.getWidth(), alignment, (x, y, e) -> e.content.draw(x, y + (height / 2) - (e.content.getWidth() / 2), e.width)));
    }

    public void setImage(int index, String image) {
        setImage(index, image, Alignment.CENTER);
    }

    public void setImage(int index, String image, Alignment alignment) {
        ImageUtil.Image img = ImageUtil.Image.of(image);
        setElement(index, new ButtonElement<>(img, img.getWidth(), alignment, (x, y, e) -> e.content.draw(x, y + (height / 2) - (e.content.getWidth() / 2), e.width)));
    }

    public void addSpacer(int width) {
        addSpacer(width, Alignment.CENTER);
    }

    public void addSpacer(int width, Alignment alignment) {
        addElement(new ButtonElement<>(Spacer.INSTANCE, width, alignment, (x, y, w) -> {}));
    }

    public void setSpacer(int index, int width) {
        setSpacer(index, width, Alignment.CENTER);
    }

    public void setSpacer(int index, int width, Alignment alignment) {
        setElement(index, new ButtonElement<>(Spacer.INSTANCE, width, alignment, (x, y, w) -> {}));
    }

    public void addElement(ButtonElement<?> element) {
        contents.add(element);
    }

    public void setElement(int index, ButtonElement<?> element) {
        contents.set(index, element);
    }

    public int size() {
        return contents.size();
    }

    protected int getYImage() {
        if (!enabled) {
            return 0;
        } else if (hovered) {
            return 40;
        }
        return 20;
    }

    @Override
    public void onMouseScroll(int mouseX, int mouseY, int wheelDelta) {

    }

    @Override
    public void onMouseDown(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) {
            return;
        }

        Minecraft.INSTANCE.soundManager.playSound("random.click", 1, 1);
        action.run();
    }

    @Override
    public void onMouseUp(int mouseX, int mouseY, int button) {

    }

    @Override
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
        this.x = bounds.getX();
        this.y = bounds.getY();
        this.width = bounds.getWidth();
        this.height = bounds.getHeight();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        hovered = bounds.contains(mouseX, mouseY);
        if (!visible) {
            return;
        }

        renderBackground();

        Map<Alignment, List<ButtonElement<?>>> sortedElems = new HashMap<>();
        sortedElems.put(Alignment.LEFT, new ArrayList<>());
        sortedElems.put(Alignment.RIGHT, new ArrayList<>());
        sortedElems.put(Alignment.CENTER, new ArrayList<>());

        for (ButtonElement<?> element : contents) {
            sortedElems.get(element.alignment).add(element);
        }

        int leftPos = x + 3;
        int centerWidth = 0;
        int rightPos = x + width - 3;

        for (ButtonElement<?> element : sortedElems.get(Alignment.CENTER)) {
            centerWidth += element.width;
        }

        int centerPos = x + (width / 2) - (centerWidth / 2);

        for (ButtonElement<?> element : sortedElems.get(Alignment.LEFT)) {
            element.render(leftPos, y);
            leftPos += element.width;
        }

        for (ButtonElement<?> element : sortedElems.get(Alignment.RIGHT)) {
            rightPos -= element.width;
            element.render(rightPos, y);
        }

        for (ButtonElement<?> element : sortedElems.get(Alignment.CENTER)) {
            element.render(centerPos, y);
            centerPos += element.width;
        }
    }

    protected void renderBackground() {
        Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId("/gui/gui.png"));
        int imageOffset = getYImage();

        drawTexture(x, y, 0, 46 + imageOffset, width / 2, height);
        drawTexture(x + width / 2, y, 200 - width / 2, 46 + imageOffset, width / 2, height / 2);
        drawTexture(x, y + height / 2, 0, 46 + imageOffset + 20 - height / 2, width / 2, height / 2);
        drawTexture(x + width / 2, y + height / 2, 200 - width / 2, 46 + imageOffset + 20 - height / 2, width / 2, height / 2);
    }

    public static class ButtonElement<T> {
        private final T content;
        private final int width;
        private final Renderer<T> renderer;
        private final Alignment alignment;

        public ButtonElement(T content, int width, Renderer<T> renderer) {
            this.content = content;
            this.width = width;
            this.alignment = Alignment.CENTER;
            this.renderer = renderer;
        }

        public ButtonElement(T content, int width, Alignment alignment, Renderer<T> renderer) {
            this.content = content;
            this.width = width;
            this.alignment = alignment;
            this.renderer = renderer;
        }

        public void render(int x, int y) {
            renderer.render(x, y, this);
        }
    }

    @FunctionalInterface
    public interface Renderer<T> {
        void render(int x, int y, ButtonElement<T> element);
    }

    public static final class Spacer {
        @SuppressWarnings("InstantiationOfUtilityClass")
        public static final Spacer INSTANCE = new Spacer();

        private Spacer() {}
    }

    public enum Alignment {
        LEFT,
        RIGHT,
        CENTER
    }
}
