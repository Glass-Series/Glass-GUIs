package net.glasslauncher.mods.glassguistest.events.init

import net.mine_diver.unsafeevents.listener.EventListener
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.item.ItemStack
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler.ScreenFactoryNoMessage
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent
import net.modificationstation.stationapi.api.recipe.CraftingRegistry
import net.modificationstation.stationapi.api.util.Identifier

class GlassGUIsTest {
    companion object {
        var testBlock: Block? = null
        var sprite: Atlas.Sprite? = null
    }

    @EventListener
    fun blocks(event: BlockRegistryEvent) {
        testBlock = GuiTestBlock(Identifier.of("glassguis_test:test_block"), Material.WOOD).setTranslationKey(Identifier.of("glassguis_test:test_block"))
    }

    @EventListener
    fun recipe(event: RecipeRegisterEvent) {
        if (event.recipeId == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type()) {
            CraftingRegistry.addShapelessRecipe(ItemStack(testBlock), Block.DIRT)
        }
    }

    @EventListener
    fun entity(event: BlockEntityRegisterEvent) {
        event.register.accept(GuiTestBlockEntity::class.java, "test_gui")
    }

    @EventListener
    fun gui(event: GuiHandlerRegistryEvent) {
        event.register(Identifier.of("glassguis_test:testgui"), GuiHandler(ScreenFactoryNoMessage { player, inventory -> GuiTestGui(player.inventory, inventory as GuiTestBlockEntity) }, { GuiTestBlockEntity() }))
    }

    @EventListener
    fun tex(event: TextureRegisterEvent) {
        sprite = Atlases.getGuiItems().addTexture(Identifier.of("glassguis_test:item/battery_slot"))
    }
}