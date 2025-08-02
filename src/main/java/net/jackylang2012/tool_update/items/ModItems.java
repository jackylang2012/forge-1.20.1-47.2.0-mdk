package net.jackylang2012.tool_update.items;

import net.jackylang2012.tool_update.ToolUpdate;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ToolUpdate.MOD_ID);


    public static void registers(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
