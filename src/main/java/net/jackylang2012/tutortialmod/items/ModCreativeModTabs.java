package net.jackylang2012.tutortialmod.items;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LclMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DREAM_TAB = CREATIVE_MODE_TABS.register("stellar_dreamscapes",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STAR_DREAM_INGOT.get()))
                    .title(Component.translatable("creativetab.stellar_dreamscapes"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.STAR_DREAM_STRIDERS.get());
                        pOutput.accept(ModItems.RAW_STAR_DREAM_INGOT.get());
                        pOutput.accept(ModItems.STAR_DREAM_INGOT.get());
                        pOutput.accept(ModItems.KUN_MEAT.get());
                        pOutput.accept(ModItems.KUN_RAW_MEAT.get());
                        pOutput.accept(ModItems.KUN_COAL.get());
                        pOutput.accept(ModItems.DIAMOND_KUN_COAL.get());
                        pOutput.accept(ModItems.KUN_BLOOD.get());
                        pOutput.accept(ModItems.DREAMSTONE_HEART.get());
                        pOutput.accept(ModItems.SIMPLE_STAR_DREAM_INGOT.get());
                        pOutput.accept(ModItems.DREAM_FLINT.get());
                        pOutput.accept(ModItems.DREAM_BREASTPLATE.get());
                        pOutput.accept(ModItems.DREAM_BOW.get());
                        pOutput.accept(ModItems.DREAM_HELMET.get());
                        pOutput.accept(ModItems.DREAM_PANS.get());
                        pOutput.accept(ModItems.DREAM_PICKAXE.get());
                        pOutput.accept(ModItems.SUPER_SWORD_OF_THE_BRAVE.get());
                        pOutput.accept(ModItems.SWORD_OF_THE_BRAVE.get());
                        pOutput.accept(ModItems.WATER_GEM.get());
                        pOutput.accept(ModItems.FIRE_GEM.get());
                        pOutput.accept(ModItems.GOLD_GEM.get());
                        pOutput.accept(ModItems.ICE_GEM.get());
                        pOutput.accept(ModItems.PLANT_GEM.get());

                        pOutput.accept(ModBlocks.DREAMSTONE_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.STAR_DREAM_BLOCK.get());




                    })

                    .build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
