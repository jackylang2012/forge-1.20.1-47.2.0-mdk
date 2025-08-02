package net.jackylang2012.tool_update.network;

import net.jackylang2012.tool_update.ToolUpdate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ToolUpdate.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, OpenEnchantChoicePacket.class,
                OpenEnchantChoicePacket::encode,
                OpenEnchantChoicePacket::decode,
                OpenEnchantChoicePacket::handle);
    }
}