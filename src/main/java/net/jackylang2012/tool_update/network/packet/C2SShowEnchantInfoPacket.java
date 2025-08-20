package net.jackylang2012.tool_update.network.packet;

import net.jackylang2012.tool_update.ToolProficiency;
import net.jackylang2012.tool_update.util.EnchantUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SShowEnchantInfoPacket {
    public C2SShowEnchantInfoPacket() {}

    public static void toBytes(C2SShowEnchantInfoPacket msg, FriendlyByteBuf buf) {}

    public static C2SShowEnchantInfoPacket fromBytes(FriendlyByteBuf buf) {
        return new C2SShowEnchantInfoPacket();
    }

    public static void handle(C2SShowEnchantInfoPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                var stack = player.getMainHandItem();
                if (!stack.isEmpty()) {
                    int proficiency = ToolProficiency.getProficiency(stack);
                    player.sendSystemMessage(EnchantUtils.createEnchantList(stack, proficiency));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
