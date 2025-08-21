package net.jackylang2012.tool_update.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.jackylang2012.tool_update.client.KeyMappingsHandler;
import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.jackylang2012.tool_update.network.NetworkHandler;
import net.jackylang2012.tool_update.network.UpgradeEffectPacket;
import net.jackylang2012.tool_update.util.KeybindManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.jackylang2012.tool_update.ToolProficiency;
import net.jackylang2012.tool_update.util.EnchantUtils;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tool_update")
                // /tool_update list
                .then(Commands.literal("list")
                        .executes(ctx -> showUpgrades(ctx.getSource()))
                )

                // 新增的show_upgrades命令
                .then(Commands.literal("show_upgrades")
                        .executes(ctx -> showUpgrades(ctx.getSource()))
                )

                // /tool_update upgrade <enchant_id> <cost> <proficiency_at_click>
                .then(Commands.literal("upgrade")
                        .then(Commands.argument("enchant_id", StringArgumentType.string())
                                .then(Commands.argument("cost", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("proficiency_at_click", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    Player player = ctx.getSource().getPlayer();
                                                    ItemStack tool = player.getMainHandItem();
                                                    if (tool.isEmpty()) {
                                                        ctx.getSource().sendFailure(Component.literal("请手持工具"));
                                                        return 0;
                                                    }

                                                    String enchantIdStr = StringArgumentType.getString(ctx, "enchant_id");
                                                    int cost = IntegerArgumentType.getInteger(ctx, "cost");
                                                    int storedProficiency = IntegerArgumentType.getInteger(ctx, "proficiency_at_click");

                                                    // 检查黑名单
                                                    if (ToolUpdateConfig.BLACKLISTED_ENCHANTS.get().contains(enchantIdStr)) {
                                                        ctx.getSource().sendFailure(Component.literal("该附魔已被禁止升级"));
                                                        return 0;
                                                    }

                                                    ResourceLocation enchantId = new ResourceLocation(enchantIdStr);
                                                    Enchantment enchant = BuiltInRegistries.ENCHANTMENT.get(enchantId);
                                                    if (enchant == null) {
                                                        ctx.getSource().sendFailure(Component.literal("未知的附魔 ID: " + enchantIdStr));
                                                        return 0;
                                                    }

                                                    int currentProficiency = ToolProficiency.getProficiency(tool);
                                                    if (currentProficiency != storedProficiency) {
                                                        ctx.getSource().sendFailure(createRefreshMessage());
                                                        return 0;
                                                    }

                                                    if (currentProficiency < cost) {
                                                        ctx.getSource().sendFailure(Component.literal("熟练度不足！当前: " + currentProficiency + "，需要: " + cost));
                                                        return 0;
                                                    }

                                                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(tool);
                                                    if (!enchants.containsKey(enchant)) {
                                                        ctx.getSource().sendFailure(Component.literal("该工具没有该附魔"));
                                                        return 0;
                                                    }

                                                    int currentLevel = enchants.get(enchant);
                                                    enchants.put(enchant, currentLevel + 1);
                                                    EnchantmentHelper.setEnchantments(enchants, tool);
                                                    ToolProficiency.decreaseProficiency(tool, cost);

                                                    // 播放效果（如果启用）
                                                    if (ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.get()) {
                                                        float volume = ToolUpdateConfig.EFFECT_VOLUME.get().floatValue();
                                                        ServerLevel level = (ServerLevel) player.level();
                                                        if (level != null) {
                                                            double x = player.getX();
                                                            double y = player.getY() + player.getEyeHeight();
                                                            double z = player.getZ();

                                                            // 中心爆发粒子 - 金色光芒
                                                            level.sendParticles(ParticleTypes.GLOW,
                                                                    x, y, z,
                                                                    80, 0.3, 0.3, 0.3, 0.15);

                                                            // 缓慢旋转的彩色光环
                                                            for (int i = 0; i < 36; i++) {
                                                                double angle = 2 * Math.PI * i / 36;
                                                                double offsetX = Math.cos(angle) * 1.8;
                                                                double offsetZ = Math.sin(angle) * 1.8;
                                                                double offsetY = Math.sin(angle * 2) * 0.3; // 波浪效果

                                                                // 彩色粒子环
                                                                level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                                                                        x + offsetX, y + offsetY, z + offsetZ,
                                                                        3, 0.1, 0.1, 0.1, 0.05);

                                                                // 发光粒子点缀
                                                                level.sendParticles(ParticleTypes.GLOW,
                                                                        x + offsetX * 0.8, y + offsetY + 0.2, z + offsetZ * 0.8,
                                                                        2, 0.05, 0.05, 0.05, 0.02);
                                                            }

                                                            // 上升的星尘粒子流 - 更加细腻
                                                            for (int i = 0; i < 25; i++) {
                                                                double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
                                                                double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;
                                                                double speedY = 0.08 + level.random.nextDouble() * 0.12;
                                                                double speedX = (level.random.nextDouble() - 0.5) * 0.05;
                                                                double speedZ = (level.random.nextDouble() - 0.5) * 0.05;

                                                                // 主上升粒子
                                                                level.sendParticles(ParticleTypes.GLOW_SQUID_INK,
                                                                        x + offsetX, y, z + offsetZ,
                                                                        4, speedX, speedY, speedZ, 0.08);

                                                                // 跟随的小粒子
                                                                level.sendParticles(ParticleTypes.END_ROD,
                                                                        x + offsetX, y, z + offsetZ,
                                                                        2, speedX * 0.8, speedY * 1.1, speedZ * 0.8, 0.06);
                                                            }

                                                            // 符文旋转效果 - 更加精致
                                                            for (int layer = 0; layer < 3; layer++) {
                                                                double layerHeight = 0.5 + layer * 0.6;
                                                                int particlesPerLayer = 24;

                                                                for (int i = 0; i < particlesPerLayer; i++) {
                                                                    double angle = 2 * Math.PI * i / particlesPerLayer + (level.getGameTime() % 100) * 0.01;
                                                                    double offsetX = Math.cos(angle) * (1.2 + layer * 0.3);
                                                                    double offsetZ = Math.sin(angle) * (1.2 + layer * 0.3);

                                                                    // 不同层的不同粒子类型
                                                                    if (layer == 0) {
                                                                        level.sendParticles(ParticleTypes.ENCHANT,
                                                                                x + offsetX, y + layerHeight, z + offsetZ,
                                                                                2, 0, -0.02, 0, 0.03);
                                                                    } else if (layer == 1) {
                                                                        level.sendParticles(ParticleTypes.WAX_ON,
                                                                                x + offsetX, y + layerHeight, z + offsetZ,
                                                                                1, 0, 0.01, 0, 0.025);
                                                                    } else {
                                                                        level.sendParticles(ParticleTypes.SCRAPE,
                                                                                x + offsetX, y + layerHeight, z + offsetZ,
                                                                                1, 0, 0.015, 0, 0.02);
                                                                    }
                                                                }
                                                            }

                                                            // 地面扩散效果
                                                            for (int i = 0; i < 30; i++) {
                                                                double angle = 2 * Math.PI * i / 30;
                                                                double distance = 1.0 + level.random.nextDouble() * 1.5;
                                                                double offsetX = Math.cos(angle) * distance;
                                                                double offsetZ = Math.sin(angle) * distance;

                                                                level.sendParticles(ParticleTypes.WITCH,
                                                                        x + offsetX, y - 0.2, z + offsetZ,
                                                                        2, 0, 0.05, 0, 0.04);
                                                            }

                                                            // 随机闪烁的亮点
                                                            for (int i = 0; i < 20; i++) {
                                                                double offsetX = (level.random.nextDouble() - 0.5) * 3.0;
                                                                double offsetY = level.random.nextDouble() * 2.5;
                                                                double offsetZ = (level.random.nextDouble() - 0.5) * 3.0;

                                                                level.sendParticles(ParticleTypes.FIREWORK,
                                                                        x + offsetX, y + offsetY, z + offsetZ,
                                                                        1, 0, 0, 0, 0.1);
                                                            }

                                                            // 音效序列 - 更加丰富的听觉体验
                                                            // 主升级音效
                                                            level.playSound(null, player.blockPosition(),
                                                                    SoundEvents.ENCHANTMENT_TABLE_USE,
                                                                    SoundSource.PLAYERS, volume * 1.2f, 0.85F + level.random.nextFloat() * 0.3F);

                                                            // 法术吟唱音效
                                                            level.playSound(null, player.blockPosition(),
                                                                    SoundEvents.ILLUSIONER_CAST_SPELL,
                                                                    SoundSource.PLAYERS, volume * 0.9f, 1.1F + level.random.nextFloat() * 0.2F);

                                                            // 成功升级音效
                                                            level.playSound(null, player.blockPosition(),
                                                                    SoundEvents.PLAYER_LEVELUP,
                                                                    SoundSource.PLAYERS, volume * 0.8f, 0.95F);

                                                            // 轻微的魔法共鸣音效
                                                            level.playSound(null, player.blockPosition(),
                                                                    SoundEvents.AMETHYST_BLOCK_CHIME,
                                                                    SoundSource.PLAYERS, volume * 0.4f, 1.4F + level.random.nextFloat() * 0.3F);

                                                            // 发送网络包到客户端显示更复杂的粒子效果
                                                            if (player instanceof ServerPlayer serverPlayer) {
                                                                NetworkHandler.INSTANCE.send(
                                                                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                                                                        new UpgradeEffectPacket()
                                                                );
                                                            }
                                                        }
                                                    }

                                                    ctx.getSource().sendSuccess(() ->
                                                                    Component.literal("成功将 ")
                                                                            .append(enchant.getFullname(currentLevel))
                                                                            .append(Component.literal(" 升级为 "))
                                                                            .append(enchant.getFullname(currentLevel + 1)),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )

                // /tool_update set_proficiency <amount>
                .then(Commands.literal("set_proficiency")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    Player player = ctx.getSource().getPlayer();
                                    ItemStack tool = player.getMainHandItem();
                                    if (tool.isEmpty()) {
                                        ctx.getSource().sendFailure(Component.literal("请手持工具"));
                                        return 0;
                                    }
                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                    ToolProficiency.setProficiency(tool, amount);
                                    ctx.getSource().sendSuccess(() ->
                                            Component.literal("已设置熟练度为 " + amount), false);
                                    return 1;
                                })
                        )
                )
        );
    }

    // 显示升级列表的公共方法
    private static int showUpgrades(CommandSourceStack source) {
        if (!(source.getEntity() instanceof Player player)) {
            source.sendFailure(Component.literal("只有玩家可以使用此命令"));
            return 0;
        }

        ItemStack tool = player.getMainHandItem();
        if (tool.isEmpty()) {
            source.sendFailure(Component.literal("请手持工具"));
            return 0;
        }

        int proficiency = ToolProficiency.getProficiency(tool);
        Component message = EnchantUtils.createEnchantList(tool, proficiency);
        source.sendSuccess(() -> message, false);
        return 1;
    }

    // 创建刷新提示消息
    private static Component createRefreshMessage() {
        return Component.literal("该升级信息已过期，请重新使用[")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(KeybindManager.getKeybind())
                        .withStyle(Style.EMPTY
                                .withColor(ChatFormatting.YELLOW)
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.literal("当前设置的快捷键").withStyle(ChatFormatting.GRAY)))))
                .append("]获取升级列表或点击")
                .append(Component.literal("这里")
                        .withStyle(Style.EMPTY
                                .withColor(ChatFormatting.BLUE)
                                .withUnderlined(true)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tool_update show_upgrades"))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.literal("点击重新显示升级列表").withStyle(ChatFormatting.GRAY)))))
                .append("重新打开")
                .withStyle(ChatFormatting.GRAY);
    }
}