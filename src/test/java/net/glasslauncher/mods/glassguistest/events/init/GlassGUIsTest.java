package net.glasslauncher.mods.glassguistest.events.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.lang.invoke.MethodHandles;

public class GlassGUIsTest {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Namespace NAMESPACE = Namespace.resolve();

    public static Block testBlock = null;
    public static Atlas.Sprite sprite = null;

    @EventListener
    public void blocks(BlockRegistryEvent event) {
        testBlock = ((StationBlock) new TestBlock(NAMESPACE.id("test_block"), Material.WOOD)).setTranslationKey(Identifier.of("glassguis_test:test_block"));
    }

    @EventListener
    public void recipe(RecipeRegisterEvent event) {
        if (event.recipeId == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type()) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(testBlock), Block.DIRT);
        }
    }

    @EventListener
    public void entity(BlockEntityRegisterEvent event) {
        event.register.accept(TestBlockEntity.class, "test_gui");
    }

    @EventListener
    public void gui(GuiHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("testgui"), new GuiHandler((player, inventory, packet) -> new TestBlockGUI(player.inventory, (TestBlockEntity) inventory), TestBlockEntity::new));
    }

    @EventListener
    public void tex(TextureRegisterEvent event) {
        sprite = Atlases.getGuiItems().addTexture(NAMESPACE.id("item/battery_slot"));
    }
}