package net.glasslauncher.mods.glassguis

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreen

fun Screen.fillInclusive(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
    fill(x1, y1, x2 + 1, y2 + 1, color)
}
