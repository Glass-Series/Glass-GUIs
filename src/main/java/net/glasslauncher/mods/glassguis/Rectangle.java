package net.glasslauncher.mods.glassguis;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class Rectangle {
    private static final Cache<String, Rectangle> RECTANGLE_CACHE = Caffeine.newBuilder().maximumSize(Short.MAX_VALUE).build();

    public final int x;
    public final int y;
    public final int x2;
    public final int y2;
    public final int width;
    public final int height;

    private Rectangle(int x, int y, int x2, int y2, int width, int height) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.width = width;
        this.height = height;
    }

    public static Rectangle of(int x, int y, int width, int height) {
        if (width < 0) {
            throw new RuntimeException("Rectangle width is negative!");
        }
        if (height < 0) {
            throw new RuntimeException("Rectangle height is negative!");
        }
        return RECTANGLE_CACHE.get(x + ";" + y + ";" + width + ";" + height + ";", k -> new Rectangle(x, y, x + width, y + height, width, height));
    }

    public static Rectangle ofAbs(int x, int y, int x2, int y2) {
        if (x2 < x) {
            throw new RuntimeException("Rectangle width is negative!");
        }
        if (y2 < y) {
            throw new RuntimeException("Rectangle height is negative!");
        }
        int width = x2 - x;
        int height = y2 - y;
        return RECTANGLE_CACHE.get(x + ";" + y + ";" + width + ";" + height + ";", k -> new Rectangle(x, y, x2, y2, width, height));
    }

    public boolean contains(int x, int y) {
        return this.x < x && this.y < y && this.x2 > x && this.y2 > y;
    }
}
