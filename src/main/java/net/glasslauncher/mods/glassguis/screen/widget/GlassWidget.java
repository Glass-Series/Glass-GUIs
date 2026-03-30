package net.glasslauncher.mods.glassguis.screen.widget;

import org.lwjgl.util.Rectangle;

public interface GlassWidget {
    void onMouseScroll(int mouseX, int mouseY, int wheelDelta);
    void onMouseDown(int mouseX, int mouseY, int button);
    void onMouseUp(int mouseX, int mouseY, int button);

    Rectangle getBounds();
    void setBounds(Rectangle bounds);

    void render(int mouseX, int mouseY, float frameDelta);
}
