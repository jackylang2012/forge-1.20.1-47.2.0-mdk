package net.jackylang2012.tutortialmod.items;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.custom.FuelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LclMod.MOD_ID);

    public static final RegistryObject<Item> STAR_DREAM_STRIDERS = ITEMS.register("star_dream_striders",
            () -> new Item(new Item.Properties().stacksTo(1)));

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
    public static final RegistryObject<Item> DREAMSTONE_HEART = ITEMS.register("dreamstone_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SIMPLE_STAR_DREAM_INGOT = ITEMS.register("simple_star_dream_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DREAM_FLINT = ITEMS.register("dream_flint",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DREAM_BREASTPLATE = ITEMS.register("dream_breastplate",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DREAM_BOW = ITEMS.register("dream_bow",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DREAM_HELMET = ITEMS.register("dream_helmet",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DREAM_PANS = ITEMS.register("dream_pans",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOLD_GEM = ITEMS.register("gold_gem",
            () -> new GoldGemItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PLANT_GEM = ITEMS.register("plant_gem",
            () -> new PlantGemItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WATER_GEM = ITEMS.register("water_gem",
            () -> new WaterGemItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ICE_GEM = ITEMS.register("ice_gem",
            () -> new IceGemItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FIRE_GEM = ITEMS.register("fire_gem",
            () -> new FireGemItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SWORD_OF_THE_BRAVE = ITEMS.register("sword_of_the_brave",
            () -> new SwordItem(ModToolTiers.DREAM, 18, 2, new Item.Properties()));
    public static final RegistryObject<Item> SUPER_SWORD_OF_THE_BRAVE = ITEMS.register("super_sword_of_the_brave",
            () -> new SwordItem(ModToolTiers.BRAVE, 28, 2, new Item.Properties()));

    public static final RegistryObject<Item> DREAM_PICKAXE = ITEMS.register("dream_pickaxe",
            () -> new PickaxeItem(ModToolTiers.DREAM, 5, 3, new Item.Properties()));


    public static final RegistryObject<Item> KUN_COAL = ITEMS.register("kun_coal",
            () -> new FuelItem(new Item.Properties(), 8000));
    public static final RegistryObject<Item> DIAMOND_KUN_COAL = ITEMS.register("diamond_kun_coal",
            () -> new FuelItem(new Item.Properties(), 80000));

    public static void registers(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
