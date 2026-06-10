package net.glasslauncher.mods.glassguis.screen.widget;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public abstract class GlassEntryListWidget implements GlassWidget {
    protected final Minecraft minecraft;
    protected int width;
    protected int height;
    protected int topEnd;
    protected int bottomStart;
    protected int right;
    protected int left;
    protected int x;
    protected int y;
    protected int topSize;
    protected int bottomSize;
    protected int itemHeight;
    protected int lastMousePos = -1;
    protected ScrollMode scrollMode = ScrollMode.NONE;
    @Setter @Getter
    protected float scrollAmount;
    protected int lastHoveredEntry = -1;
    protected long lastClicked = 0L;
    @Setter
    protected boolean drawSelectedBox = true;
    @Setter
    protected int firstEntryRenderOffset;
    protected int margin;
    @Getter
    private Rectangle bounds;
    @Getter
    private Rectangle scrollBounds;
    @Setter
    private boolean drawBackground = true;

    public GlassEntryListWidget(Minecraft minecraft, int x, int y, int width, int height, int topSize, int bottomSize, int itemHeight) {
        this.minecraft = minecraft;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.itemHeight = itemHeight;
        this.margin = 20;
        setBounds(new Rectangle(x, y, width, height));
    }

    public GlassEntryListWidget(Minecraft minecraft, int x, int y, int width, int height, int topSize, int bottomSize, int itemHeight, int margin) {
        this.minecraft = minecraft;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.itemHeight = itemHeight;
        this.margin = margin;
        setBounds(new Rectangle(x, y, width, height));
    }

    protected abstract int getEntryCount();

    protected abstract void entryClicked(int index, boolean doubleClick);

    protected abstract boolean isSelectedEntry(int index);

    protected int getEntriesHeight() {
        return getEntryCount() * itemHeight + firstEntryRenderOffset;
    }

    protected abstract void renderBackground();

    protected abstract void renderEntry(int index, int x, int y, int width, int height, Tessellator tessellator);

    public int getHoveredEntry(int mouseX, int mouseY) {
        int startX = getEntryLeft();
        int endX = getEntryRight();
        int relativeMouseY = mouseY - topEnd - firstEntryRenderOffset + (int)scrollAmount - 4;
        int hoveredEntry = relativeMouseY / itemHeight;
        return mouseX >= startX && mouseX <= endX && hoveredEntry >= 0 && relativeMouseY >= 0 && hoveredEntry < getEntryCount() ? hoveredEntry : -1;
    }

    @Override
    public void onMouseScroll(int mouseX, int mouseY, int wheelDelta) {
        if (bounds.contains(mouseX, mouseY)) {
            scroll(-(wheelDelta / 50f) * (itemHeight / 2.0f));
        }
    }

    @Override
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
        this.x = bounds.getX();
        this.y = bounds.getY();
        this.width = bounds.getWidth();
        this.height = bounds.getHeight();
        this.topEnd = y + topSize;
        this.bottomStart = y + height - bottomSize;
        this.left = x;
        this.right = x + width;
        scrollBounds = new Rectangle(x, topEnd, width, bottomStart - topSize);
    }

    public int getEntryLeft() {
        return x + margin;
    }

    public int getEntryRight() {
        return getEntryLeft() + width - (margin * 2);
    }

    public void scroll(float amount) {
        scrollAmount += amount;
        clampScroll();
    }

    protected void clampScroll() {
        int maxScroll = getEntriesHeight() - (bottomStart - topEnd - 4);
        if (maxScroll < 0) {
            maxScroll /= 2;
        }

        if (scrollAmount < 0.0F) {
            scrollAmount = 0.0F;
        }

        if (scrollAmount > (float)maxScroll) {
            scrollAmount = (float)maxScroll;
        }

    }

    @Override
    public void onMouseDown(int mouseX, int mouseY, int button) {
        if (button != 0 || !scrollBounds.contains(mouseX, mouseY)) {
            return;
        }

        int scrollbarStart = x + width - 6;
        int scrollbarEnd = scrollbarStart + 6;

        if (mouseX >= scrollbarStart && mouseX <= scrollbarEnd) {
            scrollMode = ScrollMode.BAR;
            return;
        }

        scrollMode = ScrollMode.BODY;

        int hoveredEntry = getHoveredEntry(mouseX, mouseY);

        if (mouseY != -1) {
            boolean doubleClicked = hoveredEntry == lastHoveredEntry && System.currentTimeMillis() - lastClicked < 250L;
            entryClicked(hoveredEntry, doubleClicked);
            lastHoveredEntry = hoveredEntry;
            lastClicked = doubleClicked ? 0 : System.currentTimeMillis(); // Don't allow chain double-clicking
        }
        clampScroll();
    }

    @Override
    public void onMouseUp(int mouseX, int mouseY, int button) {
        if (button == 0) {
            scrollMode = ScrollMode.NONE;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float frameDelta) {
        renderBackground();
        clampScroll();

        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (drawBackground) {
            float backgroundSize = 32.0F;
            tessellator.startQuads();
            tessellator.color(2105376);
            tessellator.vertex(left, bottomStart, 0.0, (float) left / backgroundSize, (float) (y + bottomStart + (int) scrollAmount) / backgroundSize);
            tessellator.vertex(right, bottomStart, 0.0, (float) right / backgroundSize, (float) (y + bottomStart + (int) scrollAmount) / backgroundSize);
            tessellator.vertex(right, topEnd, 0.0, (float) right / backgroundSize, (float) (y + topEnd + (int) scrollAmount) / backgroundSize);
            tessellator.vertex(left, topEnd, 0.0, (float) left / backgroundSize, (float) (y + topEnd + (int) scrollAmount) / backgroundSize);
            tessellator.draw();
        }

        int entryCount = getEntryCount();
        int entryHeight = itemHeight - 4;
        int scrollAreaStartX = getEntryLeft();
        int bodyTop = topEnd + 4 - (int)scrollAmount;

        if (scrollMode != ScrollMode.NONE && mouseY >= topEnd && mouseY <= bottomStart) {
            if (scrollMode == ScrollMode.BODY) {
                if (lastMousePos != -1) {
                    scroll(lastMousePos - mouseY);
                }
                lastMousePos = mouseY;
            }
            else if (scrollMode == ScrollMode.BAR) {
                int bodyHeight = getEntriesHeight() - (bottomStart - topEnd - 4);
                if (bodyHeight < 1) {
                    bodyHeight = 1;
                }

                int scrollbarLength = (int)((float)((bottomStart - topEnd) * (bottomStart - topEnd)) / (float)getEntriesHeight());
                if (scrollbarLength < 32) {
                    scrollbarLength = 32;
                }

                if (scrollbarLength > bottomStart - topEnd - 8) {
                    scrollbarLength = bottomStart - topEnd - 8;
                }

                int barMiddleStart = topEnd + (scrollbarLength / 2);
                int barMiddleEnd = bottomStart - (scrollbarLength / 2);
                int barMiddleArea = barMiddleEnd - barMiddleStart;
                int mouseBarPos = mouseY - barMiddleStart;

                // It's not *perfect*, but it's close enough.
                scrollAmount = bodyHeight * ((float) mouseBarPos / barMiddleArea);
                lastMousePos = -1;
                clampScroll();
            }
        } else {
            lastMousePos = -1;
        }

        for(int currentEntry = 0; currentEntry < entryCount; ++currentEntry) {
            int entryTop = bodyTop + currentEntry * itemHeight + firstEntryRenderOffset;
            if (entryTop <= bottomStart && entryTop + entryHeight >= topEnd) {
                if (drawSelectedBox && isSelectedEntry(currentEntry)) {
                    int entryStartX = getEntryLeft();
                    int entryEndX = getEntryRight();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(3553);
                    tessellator.startQuads();
                    tessellator.color(8421504);
                    tessellator.vertex(entryStartX, entryTop + entryHeight + 2, 0.0, 0.0, 1.0);
                    tessellator.vertex(entryEndX, entryTop + entryHeight + 2, 0.0, 1.0, 1.0);
                    tessellator.vertex(entryEndX, entryTop - 2, 0.0, 1.0, 0.0);
                    tessellator.vertex(entryStartX, entryTop - 2, 0.0, 0.0, 0.0);
                    tessellator.color(0);
                    tessellator.vertex(entryStartX + 1, entryTop + entryHeight + 1, 0.0, 0.0, 1.0);
                    tessellator.vertex(entryEndX - 1, entryTop + entryHeight + 1, 0.0, 1.0, 1.0);
                    tessellator.vertex(entryEndX - 1, entryTop - 1, 0.0, 1.0, 0.0);
                    tessellator.vertex(entryStartX + 1, entryTop - 1, 0.0, 0.0, 0.0);
                    tessellator.draw();
                    GL11.glEnable(3553);
                }

                renderEntry(currentEntry, scrollAreaStartX, entryTop, width - (margin * 2), entryHeight, tessellator);
            }
        }

        GL11.glDisable(2929);
        byte shadowHeight = 4;
        if (topSize != 0) {
            renderBars(y, topEnd, 255, 255);
        }
        if (bottomSize != 0) {
            renderBars(bottomStart, y + height, 255, 255);
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);

        if (topSize != 0) {
            tessellator.startQuads();
            tessellator.color(0, 0);
            tessellator.vertex(left, topEnd + shadowHeight, 0.0, 0.0, 1.0);
            tessellator.vertex(right, topEnd + shadowHeight, 0.0, 1.0, 1.0);
            tessellator.color(0, 255);
            tessellator.vertex(right, topEnd, 0.0, 1.0, 0.0);
            tessellator.vertex(left, topEnd, 0.0, 0.0, 0.0);
            tessellator.draw();
        }
        if (bottomSize != 0) {
        tessellator.startQuads();
        tessellator.color(0, 255);
        tessellator.vertex(left, bottomStart, 0.0, 0.0, 1.0);
        tessellator.vertex(right, bottomStart, 0.0, 1.0, 1.0);
        tessellator.color(0, 0);
        tessellator.vertex(right, bottomStart - shadowHeight, 0.0, 1.0, 0.0);
        tessellator.vertex(left, bottomStart - shadowHeight, 0.0, 0.0, 0.0);
        tessellator.draw();
        }

        int scrollbarLeft = x + width - 6;
        int scrollbarRight = scrollbarLeft + 6;

        int bodyHeight = getEntriesHeight() - (bottomStart - topEnd - 4);
        if (bodyHeight > 0) {
            int scrollbarLength = (bottomStart - topEnd) * (bottomStart - topEnd) / getEntriesHeight();
            if (scrollbarLength < 32) {
                scrollbarLength = 32;
            }

            if (scrollbarLength > bottomStart - topEnd - 8) {
                scrollbarLength = bottomStart - topEnd - 8;
            }

            int scrollbarTop = (int)scrollAmount * (bottomStart - topEnd - scrollbarLength) / bodyHeight + topEnd;
            if (scrollbarTop < topEnd) {
                scrollbarTop = topEnd;
            }

            tessellator.startQuads();
            tessellator.color(0, 255);
            tessellator.vertex(scrollbarLeft, bottomStart, 0.0, 0.0, 1.0);
            tessellator.vertex(scrollbarRight, bottomStart, 0.0, 1.0, 1.0);
            tessellator.vertex(scrollbarRight, topEnd, 0.0, 1.0, 0.0);
            tessellator.vertex(scrollbarLeft, topEnd, 0.0, 0.0, 0.0);
            tessellator.draw();
            tessellator.startQuads();
            tessellator.color(8421504, 255);
            tessellator.vertex(scrollbarLeft, scrollbarTop + scrollbarLength, 0.0, 0.0, 1.0);
            tessellator.vertex(scrollbarRight, scrollbarTop + scrollbarLength, 0.0, 1.0, 1.0);
            tessellator.vertex(scrollbarRight, scrollbarTop, 0.0, 1.0, 0.0);
            tessellator.vertex(scrollbarLeft, scrollbarTop, 0.0, 0.0, 0.0);
            tessellator.draw();
            tessellator.startQuads();
            tessellator.color(12632256, 255);
            tessellator.vertex(scrollbarLeft, scrollbarTop + scrollbarLength - 1, 0.0, 0.0, 1.0);
            tessellator.vertex(scrollbarRight - 1, scrollbarTop + scrollbarLength - 1, 0.0, 1.0, 1.0);
            tessellator.vertex(scrollbarRight - 1, scrollbarTop, 0.0, 1.0, 0.0);
            tessellator.vertex(scrollbarLeft, scrollbarTop, 0.0, 0.0, 0.0);
            tessellator.draw();
        }

        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    protected void renderBars(int start, int end, int lowerOpacity, int upperOpacity) {
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float sizeOfSquareOnScreen = 32.0F;
        tessellator.startQuads();
        tessellator.color(4210752, upperOpacity);
        tessellator.vertex(x, end, 0.0, 0.0, (float)end / sizeOfSquareOnScreen);
        tessellator.vertex(x + width, end, 0.0, (float)width / sizeOfSquareOnScreen, (float)end / sizeOfSquareOnScreen);
        tessellator.color(4210752, lowerOpacity);
        tessellator.vertex(x + width, start, 0.0, (float)width / sizeOfSquareOnScreen, (float)start / sizeOfSquareOnScreen);
        tessellator.vertex(x, start, 0.0, 0.0, (float)start / sizeOfSquareOnScreen);
        tessellator.draw();
    }

    protected enum ScrollMode {
        NONE,
        BODY,
        BAR,
    }
}
