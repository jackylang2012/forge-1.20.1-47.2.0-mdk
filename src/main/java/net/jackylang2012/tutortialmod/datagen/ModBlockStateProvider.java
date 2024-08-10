package net.jackylang2012.tutortialmod.datagen;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LclMod.MOD_ID, exFileHelper);
    }

    @Override

    protected void registerStatesAndModels(){
        blockWithItem(ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK);
        blockWithItem(ModBlocks.DREAMSTONE_ORE_BLOCK);
        blockWithItem(ModBlocks.STAR_DREAM_BLOCK);


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
