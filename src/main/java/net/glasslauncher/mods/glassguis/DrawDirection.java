package net.glasslauncher.mods.glassguis;

public enum DrawDirection {
    UP(true, false, false, false),
    DOWN(false, true, false, false),
    LEFT(false, false, true, false),
    RIGHT(false, false, false, true),
    ;

    public final boolean up;
    public final boolean down;
    public final boolean left;
    public final boolean right;

    DrawDirection(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
}
