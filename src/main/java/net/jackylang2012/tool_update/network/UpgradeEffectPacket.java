package net.jackylang2012.tool_update.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpgradeEffectPacket {

    public UpgradeEffectPacket() {
        // 空构造
    }

    public static void encode(UpgradeEffectPacket msg, FriendlyByteBuf buf) {
        // 不需要数据，空实现
    }

    public static UpgradeEffectPacket decode(FriendlyByteBuf buf) {
        return new UpgradeEffectPacket();
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(UpgradeEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            LocalPlayer player = Minecraft.getInstance().player;

            if (level != null && player != null) {
                // 播放升级音效
                level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENCHANTMENT_TABLE_USE,
                        SoundSource.PLAYERS,
                        1.0F, 1.0F, false);

                // 播放粒子特效
                for (int i = 0; i < 20; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
                    double offsetY = level.random.nextDouble() * 1.0;
                    double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;
                    level.addParticle(ParticleTypes.ENCHANT, player.getX() + offsetX,
                            player.getY() + offsetY, player.getZ() + offsetZ,
                            0, 0.1, 0);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
