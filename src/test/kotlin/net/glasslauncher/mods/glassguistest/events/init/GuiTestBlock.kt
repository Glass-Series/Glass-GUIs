package net.glasslauncher.mods.glassguistest.events.init

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper
import net.modificationstation.stationapi.api.template.block.TemplateBlock
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity
import net.modificationstation.stationapi.api.util.Identifier

class GuiTestBlock(id: Identifier, mat: Material) : TemplateBlockWithEntity(id, mat) {


    override fun onUse(
        world: World?,
        x: Int,
        y: Int,
        z: Int,
        player: PlayerEntity?
    ): Boolean {
        val entity = world!!.getBlockEntity(x, y, z)!! as GuiTestBlockEntity
        GuiHelper.openGUI(player, Identifier.of("glassguis_test:testgui"), entity, GuiTestHandler(player!!.inventory, entity))
        return true
    }

    override fun createBlockEntity(): BlockEntity? {
        return GuiTestBlockEntity()
    }
}