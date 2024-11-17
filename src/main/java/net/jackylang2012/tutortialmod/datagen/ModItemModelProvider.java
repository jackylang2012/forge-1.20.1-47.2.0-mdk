package net.jackylang2012.tutortialmod.datagen;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LclMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.KUN_BLOOD);
        simpleItem(ModItems.KUN_COAL);
        simpleItem(ModItems.KUN_MEAT);
        simpleItem(ModItems.KUN_RAW_MEAT);
        simpleItem(ModItems.RAW_STAR_DREAM_INGOT);
        simpleItem(ModItems.STAR_DREAM_INGOT);
        simpleItem(ModItems.STAR_DREAM_STRIDERS);
        simpleItem(ModItems.SIMPLE_STAR_DREAM_INGOT);
        simpleItem(ModItems.DREAMSTONE_HEART);
        simpleItem(ModItems.DREAM_FLINT);
        simpleItem(ModItems.DREAM_BREASTPLATE);
        simpleItem(ModItems.DREAM_BOW);
        simpleItem(ModItems.DREAM_HELMET);
        simpleItem(ModItems.DREAM_PANS);
        simpleItem(ModItems.DIAMOND_KUN_COAL);
        simpleItem(ModItems.FIRE_GEM);
        simpleItem(ModItems.WATER_GEM);
        simpleItem(ModItems.PLANT_GEM);
        simpleItem(ModItems.GOLD_GEM);
        simpleItem(ModItems.ICE_GEM);

        handheldItem(ModItems.DREAM_PICKAXE);
        handheldItem(ModItems.SWORD_OF_THE_BRAVE);
        handheldItem(ModItems.SUPER_SWORD_OF_THE_BRAVE);


    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LclMod.MOD_ID,"item/" + item.getId().getPath()));


    }
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(LclMod.MOD_ID,"item/" + item.getId().getPath()));



    }

}
