package net.glasslauncher.mods.glassguis.mixin;

import net.glasslauncher.mods.glassguis.ExtendedScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin implements ExtendedScreenHandler {

    @Override
    public void setProperty(int syncID, long l) {
    }
}
