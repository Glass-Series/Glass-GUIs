package net.glasslauncher.mods.glassguis.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public Screen currentScreen;

    @Shadow public int displayWidth;

    @Shadow public int displayHeight;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;tickInput()V"))
    private void inputHooks(CallbackInfo ci) {
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int mouseX = Mouse.getEventX() * currentScreen.width / displayWidth;
            int mouseY = currentScreen.height - Mouse.getEventY() * currentScreen.height / displayHeight - 1;
            currentScreen.glassguis_mouseScrolled(mouseX, mouseY, dWheel);
        }
    }
}
