package net.glasslauncher.mods.glassguis;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum KeyModifier {
    SHIFT(() -> Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)),
    CTRL(() -> Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)),
    ALT(() -> Keyboard.isKeyDown(Keyboard.KEY_LMENU)),
    ALT_SHIFT(() -> Keyboard.isKeyDown(Keyboard.KEY_LMENU) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)),
    CTRL_ALT(() -> Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LMENU)),
    CTRL_SHIFT(() -> Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)),
    CTRL_ALT_SHIFT(() -> Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)),
    ;
    public static final KeyModifier[] ENTRIES_REVERSED;
    static {
        var v = new ArrayList<>(List.of(values()));
        Collections.reverse(v);
        ENTRIES_REVERSED = v.toArray(new KeyModifier[0]);
    }

    private final IsPressed isPressed;

    KeyModifier(IsPressed isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed() {
        return isPressed.isPressed();
    }

    @FunctionalInterface
    private interface IsPressed {
        boolean isPressed();
    }
}
