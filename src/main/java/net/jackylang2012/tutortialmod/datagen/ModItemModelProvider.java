package net.jackylang2012.tutortialmod.datagen;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.Moditems;
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
        simpleItem(Moditems.KUN_BLOOD);
        simpleItem(Moditems.KUN_COAL);
        simpleItem(Moditems.KUN_MEAT);
        simpleItem(Moditems.KUN_RAW_MEAT);
        simpleItem(Moditems.RAW_STAR_DREAM_INGOT);
        simpleItem(Moditems.STAR_DREAM_INGOT);
        simpleItem(Moditems.STAR_DREAM_STRIDERS);
        simpleItem(Moditems.SIMPLE_STAR_DREAM_INGOT);
        simpleItem(Moditems.DREAMSTONE_HEART);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LclMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}
