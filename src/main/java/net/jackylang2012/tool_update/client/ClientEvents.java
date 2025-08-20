package net.jackylang2012.tool_update.client;

import net.jackylang2012.tool_update.network.ModMessages;
import net.jackylang2012.tool_update.network.packet.C2SShowEnchantInfoPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyMappingsHandler.SHOW_ENCHANT_INFO.isDown()) {
            // 发送 C2S 消息通知服务端
            ModMessages.sendToServer(new C2SShowEnchantInfoPacket());
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null && mc.level != null) {
                mc.level.playSound(  // 使用 playSound 而不是 playLocalSound
                        player,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        }
    }
}