package net.glasslauncher.mods.glassguis.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.glasslauncher.mods.glassguis.GlassKeyBinding;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(KeybindsScreen.class)
public class KeybindsScreenMixin extends Screen {

    @Shadow private int selectedKeyBinding;

    @Shadow private GameOptions gameOptions;

    @Override
    public void onMouseEvent() {
        super.onMouseEvent();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (selectedKeyBinding != -1) {
            gameOptions.setKeybindKey(selectedKeyBinding, GlassKeyBinding.mouseToCode(button));
            return;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @WrapOperation(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;setKeybindKey(II)V"))
    public void hijackBinds(GameOptions instance, int binding, int keyCode, Operation<Void> original) {
        
        this.gameOptions.setKeybindKey(binding, keyCode);
        ((ButtonWidget)this.buttons.get(binding)).text = this.gameOptions.getKeybindKey(binding);
        this.selectedKeyBinding = -1;
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 200))
    public int whyButton(int constant) {
        return 0;
    }

    @ModifyConstant(method = "buttonClicked", constant = @Constant(intValue = 200))
    public int whyButton2(int constant) {
        return 0;
    }
}
