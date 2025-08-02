package net.jackylang2012.tool_update.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenEnchantChoicePacket {
    private final ItemStack tool;

    public OpenEnchantChoicePacket(ItemStack tool) {
        this.tool = tool;
    }

    public static void encode(OpenEnchantChoicePacket packet, FriendlyByteBuf buffer) {
        buffer.writeItem(packet.tool);
    }

    public static OpenEnchantChoicePacket decode(FriendlyByteBuf buffer) {
        return new OpenEnchantChoicePacket(buffer.readItem());
    }

    public static void handle(OpenEnchantChoicePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 替换为发送可点击文本到聊天栏
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendSystemMessage(Component.literal("点击升级工具")
                        .withStyle(style -> style
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/upgrade_tool"))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("点击执行升级")))
                        )
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}