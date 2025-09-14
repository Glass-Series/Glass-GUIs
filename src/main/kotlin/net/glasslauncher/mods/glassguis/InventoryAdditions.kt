package net.glasslauncher.mods.glassguis

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip
import net.glasslauncher.mods.gcapi3.api.CharacterUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.Tessellator
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases
import org.lwjgl.util.Rectangle
import java.awt.Color
import javax.imageio.ImageIO
import kotlin.math.abs

class InventoryAdditions(val width: Int = 176, val height: Int = 166) {
    companion object {
        val IMAGE_SIZE_CACHE: Cache<String, Array<Int>> = Caffeine.newBuilder().softValues().build()

        val GUI_BORDER = CharacterUtils.getIntFromColour(Color(0, 0, 0))
        val GUI_ROUNDING_LIGHT = -1
        val GUI_ROUNDING_DARK = CharacterUtils.getIntFromColour(Color(85, 85, 85))
        val GUI_BACKGROUND = CharacterUtils.getIntFromColour(Color(198, 198, 198))
        val SLOT_ROUNDING_LIGHT = GUI_ROUNDING_LIGHT
        val SLOT_ROUNDING_DARK = CharacterUtils.getIntFromColour(Color(55, 55, 55))
        val SLOT_BACKGROUND = CharacterUtils.getIntFromColour(Color(139, 139, 139))
        val TEXT_COLOR = CharacterUtils.getIntFromColour(Color(63, 63, 63))
    }

    var name: String? = null

    fun renderBackground(screen: HandledScreen) {
        val x: Int = (screen.width - screen.backgroundWidth) / 2
        val y: Int = (screen.height - screen.backgroundHeight) / 2

        drawBackgroundBox(screen, x, y, x + width, y + height)

        if (name != null) {
            Minecraft.INSTANCE.textRenderer.draw(name, x + 8, y + 6, TEXT_COLOR)
        }
    }

    fun drawBackgroundBox(screen: Screen, x: Int, y: Int, x2: Int, y2: Int) {
        screen.fill(x + 2, y + 2, x2 - 2, y2 - 2, GUI_BACKGROUND)

        screen.drawHorizontalLine(x + 2, x2 - 4, y, GUI_BORDER) // top
        screen.drawVerticalLine(x2 - 1, y + 2, y2 - 2, GUI_BORDER) // right
        screen.drawHorizontalLine(x + 3, x2 - 3, y2 - 1, GUI_BORDER) // bottom
        screen.drawVerticalLine(x, y + 1, y2 - 3, GUI_BORDER) // left

        screen.fillInclusive(x + 1, y + 1, x + 1, y + 1, GUI_BORDER) // tl
        screen.fillInclusive(x2 - 3,  y + 1, x2 - 3, y + 1, GUI_BORDER) // tr1
        screen.fillInclusive(x2 - 2,  y + 2, x2 - 2, y + 2, GUI_BORDER) // tr2
        screen.fillInclusive(x2 - 2, y2 - 2, x2 - 2, y2 - 2, GUI_BORDER) // br
        screen.fillInclusive(x + 2, y2 - 2, x + 2, y2 - 2, GUI_BORDER) // bl1
        screen.fillInclusive(x + 1, y2 - 3, x + 1, y2 - 3, GUI_BORDER) // bl2

        // light rounding
        screen.fillInclusive(x + 1, y + 2, x + 2, y2 - 4, GUI_ROUNDING_LIGHT) // l
        screen.fillInclusive(x + 2, y + 1, x2 - 4, y + 2, GUI_ROUNDING_LIGHT) // t
        screen.fillInclusive(x + 3, y + 3, x + 3, y + 3, GUI_ROUNDING_LIGHT)

        // dark rounding
        screen.fillInclusive(x2 - 3, y + 3, x2 - 2, y2 - 3, GUI_ROUNDING_DARK) // r
        screen.fillInclusive(x + 3, y2 - 3, x2 - 3, y2 - 2, GUI_ROUNDING_DARK) // b
        screen.fillInclusive(x2 - 4, y2 - 4, x2 - 4, y2 - 4, GUI_ROUNDING_DARK)
    }

    fun drawImage(screen: DrawContext, imageString: String, x: Int, y: Int) {
        drawImage(screen, imageString, x, y, 1f)
    }

    fun drawImage(screen: DrawContext, imageString: String, x: Int, y: Int, percentage: Float) {
        val baseX: Int
        val baseY: Int
        if (screen is HandledScreen) {
            baseX = x + ((screen.width - screen.backgroundWidth) / 2)
            baseY = y + ((screen.height - screen.backgroundHeight) / 2)
        }
        else {
            baseX = x
            baseY = y
        }

        val size: Array<Int> = IMAGE_SIZE_CACHE.get(imageString) {
            val image = ImageIO.read(javaClass.getResourceAsStream(imageString))
            return@get arrayOf(image.width, image.height)
        }

        Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId(imageString))
        drawTexture(baseX, baseY, abs(size[0] * percentage).toInt(), size[1], imgWidth = size[0])
    }

    fun drawSlots(screen: HandledScreen) {
        if (screen.container == null || screen.container.slots == null) {
            return
        }
        val x: Int = (screen.width - screen.backgroundWidth) / 2
        val y: Int = (screen.height - screen.backgroundHeight) / 2

        for (slot in screen.container.slots) {
            if (slot !is Slot) {
                continue
            }
            val slotX = x + slot.x
            val slotY = y + slot.y
            val width: Int
            val height: Int
            val backgroundWidth: Int
            val backgroundHeight: Int
            if (slot is CustomSizeSlot) {
                width = slot.getWidth()
                height = slot.getHeight()
                backgroundWidth = slot.getBackgroundWidth()
                backgroundHeight = slot.getBackgroundHeight()
            } else {
                width = 16
                height = 16
                backgroundWidth = 16
                backgroundHeight = 16
            }
            val slotBackgroundX = slotX - ((backgroundWidth - 16) / 2)
            val slotBackgroundY = slotY - ((backgroundHeight - 16) / 2)

            if (slot.backgroundTextureId != -1 && ((slot is CustomSizeSlot && slot.keepBackgroundTexture()) || !slot.hasStack())) {
                val sprite = Atlases.getGuiItems().getTexture(slot.backgroundTextureId)
                Minecraft.INSTANCE.textureManager.bindTexture(Minecraft.INSTANCE.textureManager.getTextureId("terrain.png"))
                drawTexture(x + slot.x, y + slot.y, sprite.width, sprite.height)
            }

            screen.fill(slotBackgroundX - 1, slotBackgroundY - 1, slotBackgroundX + backgroundWidth + 1, slotBackgroundY + backgroundHeight + 1, SLOT_BACKGROUND)
            screen.drawHorizontalLine(slotBackgroundX, slotBackgroundX + backgroundWidth - 1, slotBackgroundY - 1, SLOT_ROUNDING_DARK)
            screen.drawVerticalLine(slotBackgroundX - 1, slotBackgroundY - 2, slotBackgroundY + backgroundHeight, SLOT_ROUNDING_DARK)
            screen.drawHorizontalLine(slotBackgroundX, slotBackgroundX + backgroundWidth, slotBackgroundY + backgroundHeight, SLOT_ROUNDING_LIGHT)
            screen.drawVerticalLine(slotBackgroundX + backgroundWidth, slotBackgroundY - 1, slotBackgroundY + backgroundHeight, SLOT_ROUNDING_LIGHT)


        }
    }

    fun setupPlayerInventory(handledScreen: HandledScreen, playerInventory: PlayerInventory) {
        // 162 81
        val x = ((handledScreen.width - handledScreen.backgroundWidth) / 2) + (handledScreen.backgroundWidth / 2) + 8
        val y = (handledScreen.height / 2) + handledScreen.backgroundHeight + 1

        setupPlayerInventory(x, y, playerInventory, handledScreen.container)
    }

    fun setupPlayerInventory(x: Int, y: Int, playerInventory: PlayerInventory, screenHandler: ScreenHandler) {
        for (row in 0 until 3) {
            for (col in 0 until 9) {
                screenHandler.addSlot(
                    Slot(playerInventory, col + row * 9 + 9, x + col * 18, y - (4 - row) * 18 - 11)
                )
            }
        }

        for (hotbarSlot in 0 until 9) {
            screenHandler.addSlot(Slot(playerInventory, hotbarSlot, x + hotbarSlot * 18, y - 25))
        }
    }

    fun setupPlayerInventory(screenHandler: ScreenHandler, playerInventory: PlayerInventory) {
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) {
            setupPlayerInventory(0, 0, playerInventory, screenHandler)
        }
    }

    fun drawTexture(x: Int, y: Int, width: Int, height: Int, imgWidth: Int = width, imgHeight: Int = height, startX: Int = 0, startY: Int = 0) {
        val startU = (1.0 / imgWidth) * startX
        val startV = (1.0 / imgHeight) * startY
        val u = (1.0 / imgWidth) * width
        val v = (1.0 / imgHeight) * height
        val tessellator = Tessellator.INSTANCE
        tessellator.startQuads()
        tessellator.vertex((x + 0).toDouble(), (y + height).toDouble(), 0.0, startU, v) // bl
        tessellator.vertex((x + width).toDouble(), (y + height).toDouble(), 0.0, u, v) // br
        tessellator.vertex((x + width).toDouble(), (y + 0).toDouble(), 0.0, u, startV) // tr
        tessellator.vertex((x + 0).toDouble(), (y + 0).toDouble(), 0.0, startU, startV) // tl
        tessellator.draw()
    }

    fun drawText(screen: HandledScreen, text: String, x: Int, y: Int, color: Int) {
        val offsetX = ((screen.width - screen.backgroundWidth) / 2) + x
        val offsetY = ((screen.height - screen.backgroundHeight) / 2) + y
        Minecraft.INSTANCE.textRenderer.draw(text, offsetX, offsetY, color)
    }

    fun tooltip(screen: HandledScreen, text: List<String>, location: Rectangle, mouseX: Int, mouseY: Int) {
        val xOffset = (screen.width - screen.backgroundWidth) / 2
        val yOffset = (screen.height - screen.backgroundHeight) / 2

        if (!location.contains(mouseX - xOffset, mouseY - yOffset)) {
            return
        }

        if (FabricLoader.getInstance().isModLoaded("alwaysmoreitems")) { // Use AMI's much better tooltip system if it's installed.
            Tooltip.INSTANCE.setTooltip(text, mouseX, mouseY)
            return
        }

        // Otherwise we're rawdogging and praying nothing else is trying to draw a tooltip.
        val startX: Int = mouseX - xOffset + 12
        var startY: Int = mouseY - yOffset - 12
        var maxLineLength = 0
        for (line in text) {
            if (line.length > maxLineLength) {
                maxLineLength = line.length
            }
        }
        if (maxLineLength == 0) {
            return
        }
        screen.fill(
            startX - 3,
            startY - 3,
            startX + maxLineLength + 3,
            startY + 8 + 3,
            -1073741824
        )
        for (line in text) {
            Minecraft.INSTANCE.textRenderer.drawWithShadow(line, startX, startY, -1)
            startY += 12
        }
    }
}