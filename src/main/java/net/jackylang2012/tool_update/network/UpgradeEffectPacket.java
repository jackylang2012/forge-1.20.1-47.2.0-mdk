package net.jackylang2012.tool_update.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

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
                RandomSource random = level.random;
                double x = player.getX();
                double y = player.getY() + player.getEyeHeight();
                double z = player.getZ();

                // 播放升级音效
                level.playLocalSound(x, y, z,
                        SoundEvents.ENCHANTMENT_TABLE_USE,
                        SoundSource.PLAYERS,
                        1.0F, 0.9F + random.nextFloat() * 0.2F, false);

                level.playLocalSound(x, y, z,
                        SoundEvents.ILLUSIONER_CAST_SPELL,
                        SoundSource.PLAYERS,
                        0.7F, 1.2F + random.nextFloat() * 0.3F, false);

                // 使用延迟来确保所有粒子都能显示
                scheduleParticleEffects(level, x, y, z, random);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static void scheduleParticleEffects(ClientLevel level, double x, double y, double z, RandomSource random) {
        // 立即显示一些基础粒子
        spawnInitialEnchantParticles(level, x, y, z, random);

        // 使用 Minecraft 的定时器来延迟显示其他粒子效果
        Minecraft.getInstance().tell(() -> {
            spawnColorfulRing(level, x, y, z, random);

            Minecraft.getInstance().tell(() -> {
                spawnRisingParticles(level, x, y, z, random);

                Minecraft.getInstance().tell(() -> {
                    spawnBurstParticles(level, x, y, z, random);

                    Minecraft.getInstance().tell(() -> {
                        spawnRuneRing(level, x, y, z, random);
                    });
                });
            });
        });
    }

    private static void spawnInitialEnchantParticles(ClientLevel level, double x, double y, double z, RandomSource random) {
        // 初始附魔粒子效果 - 更明显
        for (int i = 0; i < 20; i++) {
            level.addParticle(ParticleTypes.ENCHANT,
                    x + (random.nextDouble() - 0.5) * 3.0,
                    y + (random.nextDouble() - 0.5) * 2.0,
                    z + (random.nextDouble() - 0.5) * 3.0,
                    (random.nextDouble() - 0.5) * 0.2,
                    0.1 + random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.2);
        }
    }

    private static void spawnColorfulRing(ClientLevel level, double x, double y, double z, RandomSource random) {
        int particles = 40; // 减少数量但增加可见性
        float radius = 2.0f; // 更大的半径

        for (int i = 0; i < particles; i++) {
            float angle = (float) (2 * Math.PI * i / particles);
            float offsetX = Mth.cos(angle) * radius;
            float offsetZ = Mth.sin(angle) * radius;

            float[] rgb = getRainbowColor(i, particles);

            // 使用更明显的粒子类型
            level.addParticle(ParticleTypes.ELECTRIC_SPARK,
                    x + offsetX, y, z + offsetZ,
                    0.0, 0.15, 0.0);

            // 添加彩色尘埃粒子
            level.addParticle(
                    new DustColorTransitionOptions(
                            new Vector3f(rgb[0], rgb[1], rgb[2]),
                            new Vector3f(0.5f, 0.5f, 0.5f),
                            2.0f // 更大的尺寸
                    ),
                    x + offsetX, y, z + offsetZ,
                    0.0, 0.1, 0.0
            );
        }
    }

    private static void spawnRisingParticles(ClientLevel level, double x, double y, double z, RandomSource random) {
        int particles = 40; // 增加数量

        for (int i = 0; i < particles; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2.0;
            double offsetZ = (random.nextDouble() - 0.5) * 2.0;
            double speedY = 0.15 + random.nextDouble() * 0.25;

            // 使用更明显的发光粒子
            level.addParticle(ParticleTypes.GLOW_SQUID_INK,
                    x + offsetX, y, z + offsetZ,
                    (random.nextDouble() - 0.5) * 0.1,
                    speedY,
                    (random.nextDouble() - 0.5) * 0.1);
        }
    }

    private static void spawnBurstParticles(ClientLevel level, double x, double y, double z, RandomSource random) {
        int particles = 30; // 减少数量但增加速度

        for (int i = 0; i < particles; i++) {
            double speedX = (random.nextDouble() - 0.5) * 0.8;
            double speedY = (random.nextDouble() - 0.5) * 0.8;
            double speedZ = (random.nextDouble() - 0.5) * 0.8;

            // 使用火花粒子，更明显
            level.addParticle(ParticleTypes.FIREWORK,
                    x, y + 1.0, z,
                    speedX, speedY, speedZ);
        }
    }

    private static void spawnRuneRing(ClientLevel level, double x, double y, double z, RandomSource random) {
        int particles = 30; // 增加数量
        float radius = 1.5f; // 更大的半径
        float height = 2.0f; // 更高的位置

        for (int i = 0; i < particles; i++) {
            float angle = (float) (2 * Math.PI * i / particles);
            float offsetX = Mth.cos(angle) * radius;
            float offsetZ = Mth.sin(angle) * radius;

            // 使用更明显的附魔粒子
            level.addParticle(ParticleTypes.ENCHANTED_HIT,
                    x + offsetX, y + height, z + offsetZ,
                    0, -0.05f, 0);

            // 添加额外的火花效果
            level.addParticle(ParticleTypes.WAX_ON,
                    x + offsetX * 0.8, y + height - 0.5, z + offsetZ * 0.8,
                    0, 0.03f, 0);
        }
    }

    private static float[] getRainbowColor(int index, int total) {
        float hue = (float) index / total;
        return hsvToRgb(hue, 0.95f, 1.0f); // 更高的饱和度
    }

    private static float[] hsvToRgb(float hue, float saturation, float value) {
        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h % 6) {
            case 0: return new float[]{value, t, p};
            case 1: return new float[]{q, value, p};
            case 2: return new float[]{p, value, t};
            case 3: return new float[]{p, q, value};
            case 4: return new float[]{t, p, value};
            case 5: return new float[]{value, p, q};
            default: return new float[]{1, 1, 1};
        }
    }
}