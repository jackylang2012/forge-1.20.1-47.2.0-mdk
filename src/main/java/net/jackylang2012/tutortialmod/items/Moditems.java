package net.jackylang2012.tutortialmod.items;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.custom.FuelItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Moditems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LclMod.MOD_ID);

    public static final RegistryObject<Item> STAR_DREAM_STRIDERS = ITEMS.register("star_dream_striders",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STAR_DREAM_INGOT = ITEMS.register("star_dream_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_STAR_DREAM_INGOT = ITEMS.register("raw_star_dream_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KUN_MEAT = ITEMS.register("kun_meat",
            () -> new Item(new Item.Properties().food(ModFoods.KUN_MEAT)));
    public static final RegistryObject<Item> KUN_RAW_MEAT = ITEMS.register("kun_raw_meat",
            () -> new Item(new Item.Properties().food(ModFoods.KUN_RAW_MEAT)));
    public static final RegistryObject<Item> KUN_BLOOD = ITEMS.register("kun_blood",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KUN_COAL = ITEMS.register("kun_coal",
            () -> new FuelItem(new Item.Properties(), 36000));

    public static void registers(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
