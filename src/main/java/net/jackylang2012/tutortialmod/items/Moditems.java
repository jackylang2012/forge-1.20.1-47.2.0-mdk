package net.jackylang2012.tutortialmod.items;

import net.jackylang2012.tutortialmod.LclMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Moditems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LclMod.MOD_ID);

    public static final RegistryObject<Item> Soul = ITEMS.register("soul",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> Raw_Sirloin = ITEMS.register("raw_sirloin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> Sirloin = ITEMS.register("sirloin",
            () -> new Item(new Item.Properties()));

    public static void registers(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
