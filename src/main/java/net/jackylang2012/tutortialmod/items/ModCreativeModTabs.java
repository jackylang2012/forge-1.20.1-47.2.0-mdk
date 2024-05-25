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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Moditems.Soul.get()))
                    .title(Component.translatable("creativetab.stellar_dreamscapes"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(Moditems.Soul.get());


                    })

                    .build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
