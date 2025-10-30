package net.glasslauncher.mods.glassguis.screen.widget;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public abstract class GlassEntryListWidget {
    protected final Minecraft minecraft;
    protected final int width;
    protected final int height;
    protected final int top;
    protected final int bottom;
    protected final int right;
    protected final int left;
    protected final int itemHeight;
    protected int scrollUpButtonId;
    protected int scrollDownButtonId;
    // -2 means do nothing, -1 means get ready for next click, and anything positive is the amount to scroll by. It's weird.
    protected float scrollMode = -2.0F;
    protected float scrollDirection;
    @Setter @Getter
    protected float scrollAmount;
    protected int lastHoveredEntry = -1;
    protected long lastClicked = 0L;
    @Setter
    protected boolean drawSelectedBox = true;
    @Setter
    protected int firstEntryRenderOffset;
    protected final int margin;

    public GlassEntryListWidget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.itemHeight = itemHeight;
        this.left = 0;
        this.right = width;
        this.margin = 20;
    }

    protected abstract int getEntryCount();

    protected abstract void entryClicked(int index, boolean doubleClick);

    protected abstract boolean isSelectedEntry(int index);

    protected int getEntriesHeight() {
        return this.getEntryCount() * this.itemHeight + this.firstEntryRenderOffset;
    }

    protected abstract void renderBackground();

    protected abstract void renderEntry(int index, int x, int width, int y, int i, Tessellator tessellator);

    public int getHoveredEntry(int mouseX, int mouseY) {
        int endX = getWidgetRight();
        int startX = getWidgetLeft();
        int relativeMouseY = mouseY - this.top - this.firstEntryRenderOffset + (int)this.scrollAmount - 4;
        int hoveredEntry = relativeMouseY / this.itemHeight;
        return mouseX >= endX && mouseX <= startX && hoveredEntry >= 0 && relativeMouseY >= 0 && hoveredEntry < this.getEntryCount() ? hoveredEntry : -1;
    }

    public int getWidgetLeft() {
        return margin;
    }

    public int getWidgetRight() {
        return width - margin;
    }

    public void setScrollButtons(int scrollUp, int scrollDown) {
        this.scrollUpButtonId = scrollUp;
        this.scrollDownButtonId = scrollDown;
    }

    public void scroll(float amount) {
        scrollAmount += amount;
    }

    protected void clampScroll() {
        int maxScroll = this.getEntriesHeight() - (this.bottom - this.top - 4);
        if (maxScroll < 0) {
            maxScroll /= 2;
        }

        if (this.scrollAmount < 0.0F) {
            this.scrollAmount = 0.0F;
        }

        if (this.scrollAmount > (float)maxScroll) {
            this.scrollAmount = (float)maxScroll;
        }

    }

    public void onMouseClicked(int mouseX, int mouseY, int button) {
        if (button != 0) {
            return;
        }
        if (this.scrollMode >= 0.0F) {
            this.scrollAmount -= ((float)mouseY - this.scrollMode) * this.scrollDirection;
            this.scrollMode = (float)mouseY;
            return;
        }
        boolean shouldScroll = true;
        if (mouseY >= this.top && mouseY <= this.bottom) {
            int scrollbarStart = this.width - 6;
            int scrollbarEnd = scrollbarStart + 6;
            int scrollAreaEndX = getWidgetLeft();
            int scrollAreaStartX = getWidgetRight();
            int relativeMousePosition = mouseY - this.top - this.firstEntryRenderOffset + (int)this.scrollAmount - 4;
            int hoveredEntry = relativeMousePosition / this.itemHeight;
            if (mouseX >= scrollAreaEndX && mouseX <= scrollAreaStartX && hoveredEntry >= 0 && relativeMousePosition >= 0 && hoveredEntry < getEntryCount()) {
                boolean doubleClicked = hoveredEntry == this.lastHoveredEntry && System.currentTimeMillis() - this.lastClicked < 250L;
                this.entryClicked(hoveredEntry, doubleClicked);
                this.lastHoveredEntry = hoveredEntry;
                this.lastClicked = doubleClicked ? 0 : System.currentTimeMillis(); // Don't allow chain double-clicking
            } else if (mouseX >= scrollAreaEndX && mouseX <= scrollAreaStartX && relativeMousePosition < 0) {
                shouldScroll = false;
            }

            if (mouseX >= scrollbarStart && mouseX <= scrollbarEnd) {
                this.scrollDirection = -1.0F;
                int var19 = this.getEntriesHeight() - (this.bottom - this.top - 4);
                if (var19 < 1) {
                    var19 = 1;
                }

                int var13 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getEntriesHeight());
                if (var13 < 32) {
                    var13 = 32;
                }

                if (var13 > this.bottom - this.top - 8) {
                    var13 = this.bottom - this.top - 8;
                }

                this.scrollDirection /= (float)(this.bottom - this.top - var13) / (float)var19;
            } else {
                this.scrollDirection = 1.0F;
            }

            if (shouldScroll) {
                this.scrollMode = (float)mouseY;
            } else {
                this.scrollMode = -2.0F;
            }
        } else {
            this.scrollMode = -2.0F;
        }
    }

    public void onMouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            this.scrollMode = -1.0F;
        }
    }

    public void buttonClicked(ButtonWidget button) {
        if (button.active) {
            if (button.id == this.scrollUpButtonId) {
                this.scrollAmount -= (float) (this.itemHeight * 2 / 3);
                this.scrollMode = -2.0F;
                this.clampScroll();
            } else if (button.id == this.scrollDownButtonId) {
                this.scrollAmount += (float)(this.itemHeight * 2 / 3);
                this.scrollMode = -2.0F;
                this.clampScroll();
            }

        }
    }

    public void render(int mouseX, int mouseY, float f) {
        this.renderBackground();
        this.clampScroll();

        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        float backgroundSize = 32.0F;
        tessellator.startQuads();
        tessellator.color(2105376);
        tessellator.vertex(this.left, this.bottom, 0.0, (float)this.left / backgroundSize, (float)(this.bottom + (int)this.scrollAmount) / backgroundSize);
        tessellator.vertex(this.right, this.bottom, 0.0, (float)this.right / backgroundSize, (float)(this.bottom + (int)this.scrollAmount) / backgroundSize);
        tessellator.vertex(this.right, this.top, 0.0, (float)this.right / backgroundSize, (float)(this.top + (int)this.scrollAmount) / backgroundSize);
        tessellator.vertex(this.left, this.top, 0.0, (float)this.left / backgroundSize, (float)(this.top + (int)this.scrollAmount) / backgroundSize);
        tessellator.draw();

        int entryCount = this.getEntryCount();
        int entryHeight = this.itemHeight - 4;
        int scrollAreaStartX = getWidgetLeft() + 4;
        int bodyTop = this.top + 4 - (int)this.scrollAmount;

        for(int hoveredEntry = 0; hoveredEntry < entryCount; ++hoveredEntry) {
            int entryTop = bodyTop + hoveredEntry * this.itemHeight + this.firstEntryRenderOffset;
            if (entryTop <= this.bottom && entryTop + entryHeight >= this.top) {
                if (this.drawSelectedBox && this.isSelectedEntry(hoveredEntry)) {
                    int entryEndX = getWidgetRight();
                    int entryStartX = getWidgetLeft();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(3553);
                    tessellator.startQuads();
                    tessellator.color(8421504);
                    tessellator.vertex(entryEndX, entryTop + entryHeight + 2, 0.0, 0.0, 1.0);
                    tessellator.vertex(entryStartX, entryTop + entryHeight + 2, 0.0, 1.0, 1.0);
                    tessellator.vertex(entryStartX, entryTop - 2, 0.0, 1.0, 0.0);
                    tessellator.vertex(entryEndX, entryTop - 2, 0.0, 0.0, 0.0);
                    tessellator.color(0);
                    tessellator.vertex(entryEndX + 1, entryTop + entryHeight + 1, 0.0, 0.0, 1.0);
                    tessellator.vertex(entryStartX - 1, entryTop + entryHeight + 1, 0.0, 1.0, 1.0);
                    tessellator.vertex(entryStartX - 1, entryTop - 1, 0.0, 1.0, 0.0);
                    tessellator.vertex(entryEndX + 1, entryTop - 1, 0.0, 0.0, 0.0);
                    tessellator.draw();
                    GL11.glEnable(3553);
                }

                this.renderEntry(hoveredEntry, scrollAreaStartX, getWidgetRight() - (getWidgetLeft() * 2), entryTop, entryHeight, tessellator);
            }
        }

        GL11.glDisable(2929);
        byte shadowHeight = 4;
        this.renderBars(0, this.top, 255, 255);
        this.renderBars(this.bottom, this.height, 255, 255);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        tessellator.startQuads();
        tessellator.color(0, 0);
        tessellator.vertex(this.left, this.top + shadowHeight, 0.0, 0.0, 1.0);
        tessellator.vertex(this.right, this.top + shadowHeight, 0.0, 1.0, 1.0);
        tessellator.color(0, 255);
        tessellator.vertex(this.right, this.top, 0.0, 1.0, 0.0);
        tessellator.vertex(this.left, this.top, 0.0, 0.0, 0.0);
        tessellator.draw();
        tessellator.startQuads();
        tessellator.color(0, 255);
        tessellator.vertex(this.left, this.bottom, 0.0, 0.0, 1.0);
        tessellator.vertex(this.right, this.bottom, 0.0, 1.0, 1.0);
        tessellator.color(0, 0);
        tessellator.vertex(this.right, this.bottom - shadowHeight, 0.0, 1.0, 0.0);
        tessellator.vertex(this.left, this.bottom - shadowHeight, 0.0, 0.0, 0.0);
        tessellator.draw();

        int scrollbarLeft = this.width - 6;
        int scrollbarRight = scrollbarLeft + 6;

        int bodyHeight = this.getEntriesHeight() - (this.bottom - this.top - 4);
        if (bodyHeight > 0) {
            int scrollbarLength = (this.bottom - this.top) * (this.bottom - this.top) / this.getEntriesHeight();
            if (scrollbarLength < 32) {
                scrollbarLength = 32;
            }

            if (scrollbarLength > this.bottom - this.top - 8) {
                scrollbarLength = this.bottom - this.top - 8;
            }

            int scrollbarTop = (int)this.scrollAmount * (this.bottom - this.top - scrollbarLength) / bodyHeight + this.top;
            if (scrollbarTop < this.top) {
                scrollbarTop = this.top;
            }

            tessellator.startQuads();
            tessellator.color(0, 255);
            tessellator.vertex(scrollbarLeft, this.bottom, 0.0, 0.0, 1.0);
            tessellator.vertex(scrollbarRight, this.bottom, 0.0, 1.0, 1.0);
            tessellator.vertex(scrollbarRight, this.top, 0.0, 1.0, 0.0);
            tessellator.vertex(scrollbarLeft, this.top, 0.0, 0.0, 0.0);
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
        GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float sizeOfSquareOnScreen = 32.0F;
        tessellator.startQuads();
        tessellator.color(4210752, upperOpacity);
        tessellator.vertex(0.0, end, 0.0, 0.0, (float)end / sizeOfSquareOnScreen);
        tessellator.vertex(this.width, end, 0.0, (float)this.width / sizeOfSquareOnScreen, (float)end / sizeOfSquareOnScreen);
        tessellator.color(4210752, lowerOpacity);
        tessellator.vertex(this.width, start, 0.0, (float)this.width / sizeOfSquareOnScreen, (float)start / sizeOfSquareOnScreen);
        tessellator.vertex(0.0, start, 0.0, 0.0, (float)start / sizeOfSquareOnScreen);
        tessellator.draw();
    }
}
