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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Moditems.STAR_DREAM_INGOT.get()))
                    .title(Component.translatable("creativetab.stellar_dreamscapes"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(Moditems.STAR_DREAM_STRIDERS.get());
                        pOutput.accept(Moditems.RAW_STAR_DREAM_INGOT.get());
                        pOutput.accept(Moditems.STAR_DREAM_INGOT.get());
                        pOutput.accept(Moditems.KUN_MEAT.get());
                        pOutput.accept(Moditems.KUN_RAW_MEAT.get());
                        pOutput.accept(Moditems.KUN_COAL.get());
                        pOutput.accept(Moditems.KUN_BLOOD.get());

                        pOutput.accept(ModBlocks.DREAMSTONE_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.STAR_DREAM_BLOCK.get());



                    })

                    .build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
