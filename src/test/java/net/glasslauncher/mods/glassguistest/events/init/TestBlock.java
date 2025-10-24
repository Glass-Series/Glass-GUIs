package net.glasslauncher.mods.glassguistest.events.init;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class TestBlock extends TemplateBlockWithEntity {

    public TestBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public boolean onUse(
        World world,
        int x,
        int y,
        int z,
        PlayerEntity player
    ) {
        TestBlockEntity entity = (TestBlockEntity) world.getBlockEntity(x, y, z);
        GuiHelper.openGUI(player, Identifier.of("glassguis_test:testgui"), entity, new TestBlockHandler(player.inventory, entity));
        return true;
    }

    @Override
    public BlockEntity createBlockEntity() {
        return new TestBlockEntity();
    }
}