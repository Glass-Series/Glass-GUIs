package net.glasslauncher.mods.glassguis;

import lombok.Setter;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GlassKeyBinding {
    public static final int MOUSE_1 = Keyboard.KEY_SLEEP + 1;
    public static final int MOUSE_2 = MOUSE_1 + 1;
    public static final int MOUSE_MIDDLE = MOUSE_2 + 1;

    public final String translationKey;
    @Setter
    private int code;
    @Setter
    private int modifier;

    public GlassKeyBinding(String translationKey, int defaultCode, int defaultModifier) {
        this.translationKey = translationKey;
        this.code = defaultCode;
        this.modifier = defaultModifier;
    }

    public GlassKeyBinding(String translationKey, int defaultCode) {
        this.translationKey = translationKey;
        this.code = defaultCode;
        this.modifier = Keyboard.KEY_NONE;
    }

    public String getName() {
        return TranslationStorage.getInstance().get(translationKey);
    }

    public boolean isDown() {
        if (code > Keyboard.KEY_SLEEP) {
            return Mouse.isButtonDown(code - MOUSE_1) && Keyboard.isKeyDown(modifier);
        }
        return Keyboard.isKeyDown(code) && (modifier == Keyboard.KEY_NONE || Keyboard.isKeyDown(modifier));
    }

    public static int mouseToCode(int mouse) {
        return MOUSE_1 + mouse;
    }
}
