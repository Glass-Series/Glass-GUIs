package net.glasslauncher.mods.glassguis.screen.widget;

import lombok.Getter;
import lombok.Setter;
import net.glasslauncher.mods.alwaysmoreitems.gui.AMITextRenderer;
import net.glasslauncher.mods.gcapi3.api.CharacterUtils;
import net.glasslauncher.mods.glassguis.ImageUtil;
import net.glasslauncher.mods.glassguis.screen.widget.element.ImageElement;
import net.glasslauncher.mods.glassguis.screen.widget.element.WidgetElement;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlassButton extends GlassBaseWidget {
    @Getter @Setter
    private Runnable action;
    @Getter @Setter
    private int color = CharacterUtils.getIntFromColour(new Color(224, 224, 224));
    @Getter @Setter
    private int hoveredColor = CharacterUtils.getIntFromColour(new Color(255, 255, 160));
    @Getter @Setter
    private int disabledColor = CharacterUtils.getIntFromColour(new Color(255, 160, 160, 160));


    public GlassButton(int x, int y, String text, Runnable action) {
        this(x, y, 200, 20, text, action);
    }

    public GlassButton(int x, int y, String text, WidgetElement.Alignment alignment, Runnable action) {
        this(x, y, 200, 20, text, alignment, action);
    }

    public GlassButton(int x, int y, int width, int height, String text, Runnable action) {
        this(x, y, width, height, text, WidgetElement.Alignment.CENTER, action);
    }

    public GlassButton(int x, int y, int width, int height, String text, WidgetElement.Alignment alignment, Runnable action) {
        super(x, y, width, height);
        this.action = action;
        addText(text, alignment);
    }

    public GlassButton(int x, int y, int width, int height, String image, String text, Runnable action) {
        this(x, y, width, height, text, WidgetElement.Alignment.CENTER, action);
    }

    public GlassButton(int x, int y, int width, int height, String image, String text, WidgetElement.Alignment alignment, Runnable action) {
        super(x, y, width, height);
        this.action = action;
        addImage(image);
        addText(text, alignment);
    }

    public void addText(String text) {
        addText(text, WidgetElement.Alignment.CENTER);
    }

    public void addText(String text, WidgetElement.Alignment alignment) {
        WidgetElement<String> element = new WidgetElement<>(text, Minecraft.INSTANCE.textRenderer.getWidth(text), AMITextRenderer.FONT_HEIGHT, alignment, (x, y, w, h, e) -> Minecraft.INSTANCE.textRenderer.drawWithShadow(text, x, y, enabled ? hovered ? hoveredColor : color : disabledColor));
        addElement(element);
    }

    public void setText(int index, String text) {
        setText(index, text, WidgetElement.Alignment.CENTER);
    }

    public void setText(int index, String text, WidgetElement.Alignment alignment) {
        WidgetElement<String> element = new WidgetElement<>(text, Minecraft.INSTANCE.textRenderer.getWidth(text), AMITextRenderer.FONT_HEIGHT, alignment, (x, y, w, h, e) -> Minecraft.INSTANCE.textRenderer.drawWithShadow(e.getContent(), x, y, enabled ? hovered ? hoveredColor : color : disabledColor));
        setElement(index, element);
    }

    public void addImage(String image) {
        addImage(image, WidgetElement.Alignment.CENTER);
    }

    public void addImage(String image, WidgetElement.Alignment alignment) {
        ImageUtil.Image img = ImageUtil.Image.of(image);
        addElement(new ImageElement(img, img.getWidth(), img.getHeight(), alignment));
    }

    public void setImage(int index, String image) {
        setImage(index, image, WidgetElement.Alignment.CENTER);
    }

    public void setImage(int index, String image, WidgetElement.Alignment alignment) {
        ImageUtil.Image img = ImageUtil.Image.of(image);
        setElement(index, new ImageElement(img, img.getWidth(), img.getHeight(), alignment));
    }

    public void addSpacer(int width) {
        addSpacer(width, WidgetElement.Alignment.CENTER);
    }

    public void addSpacer(int width, WidgetElement.Alignment alignment) {
        addElement(new WidgetElement<>(null, width, 1, alignment, (x, y, w, h, e) -> {}));
    }

    public void setSpacer(int index, int width) {
        setSpacer(index, width, WidgetElement.Alignment.CENTER);
    }

    public void setSpacer(int index, int width, WidgetElement.Alignment alignment) {
        setElement(index, new WidgetElement<>(null, width, 1, alignment, (x, y, w, h, e) -> {}));
    }

    public int size() {
        return elements.size();
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
    public void renderElements(int mouseX, int mouseY, float delta) {
        Map<WidgetElement.Alignment, List<WidgetElement<?>>> sortedElems = new HashMap<>();
        sortedElems.put(WidgetElement.Alignment.LEFT, new ArrayList<>());
        sortedElems.put(WidgetElement.Alignment.RIGHT, new ArrayList<>());
        sortedElems.put(WidgetElement.Alignment.CENTER, new ArrayList<>());

        for (WidgetElement<?> element : elements) {
            if (sortedElems.containsKey(element.getAlignment())) {
                sortedElems.get(element.getAlignment()).add(element);
                continue;
            }
            sortedElems.get(WidgetElement.Alignment.CENTER).add(element);
        }

        int leftPos = x + 3;
        int centerWidth = 0;
        int rightPos = x + width - 3;

        for (WidgetElement<?> element : sortedElems.get(WidgetElement.Alignment.CENTER)) {
            centerWidth += element.getWidth();
        }

        int centerPos = x + (width / 2) - (centerWidth / 2);
        int elemY = y + (height / 2);

        for (WidgetElement<?> element : sortedElems.get(WidgetElement.Alignment.LEFT)) {
            element.render(leftPos, elemY - (element.getHeight() / 2), element.getWidth(), element.getHeight());
            leftPos += element.getWidth();
        }

        for (WidgetElement<?> element : sortedElems.get(WidgetElement.Alignment.RIGHT)) {
            rightPos -= element.getWidth();
            element.render(rightPos, elemY - (element.getHeight() / 2), element.getWidth(), element.getHeight());
        }

        for (WidgetElement<?> element : sortedElems.get(WidgetElement.Alignment.CENTER)) {
            element.render(centerPos, elemY - (element.getHeight() / 2), element.getWidth(), element.getHeight());
            centerPos += element.getWidth();
        }
    }

    @Override
    protected void renderBackground(int mouseX, int mouseY, float delta) {
        Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId("/gui/gui.png"));
        int imageOffset = getYImage();

        drawTexture(x, y, 0, 46 + imageOffset, width / 2, height);
        drawTexture(x + width / 2, y, 200 - width / 2, 46 + imageOffset, width / 2, height / 2);
        drawTexture(x, y + height / 2, 0, 46 + imageOffset + 20 - height / 2, width / 2, height / 2);
        drawTexture(x + width / 2, y + height / 2, 200 - width / 2, 46 + imageOffset + 20 - height / 2, width / 2, height / 2);
    }
}
