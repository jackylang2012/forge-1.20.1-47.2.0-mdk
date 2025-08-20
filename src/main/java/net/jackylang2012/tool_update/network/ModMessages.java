package net.jackylang2012.tool_update.network;

import net.jackylang2012.tool_update.ToolUpdate;
import net.jackylang2012.tool_update.network.packet.C2SShowEnchantInfoPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ToolUpdate.MOD_ID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        INSTANCE.registerMessage(
                id(), C2SShowEnchantInfoPacket.class,
                C2SShowEnchantInfoPacket::toBytes,
                C2SShowEnchantInfoPacket::fromBytes,
                C2SShowEnchantInfoPacket::handle
        );
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
