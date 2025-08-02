package net.jackylang2012.tool_update.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.jackylang2012.tool_update.ToolProficiency;
import net.jackylang2012.tool_update.util.EnchantUtils;

import java.util.Map;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tool_update")
                // /tool_update list
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            Player player = ctx.getSource().getPlayer();
                            ItemStack tool = player.getMainHandItem();
                            if (tool.isEmpty()) {
                                ctx.getSource().sendFailure(Component.literal("请手持工具"));
                                return 0;
                            }

                            int proficiency = ToolProficiency.getProficiency(tool);
                            Component message = EnchantUtils.createEnchantList(tool, proficiency);
                            ctx.getSource().sendSuccess(() -> message, false);
                            return 1;
                        })
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
                                                        ctx.getSource().sendFailure(Component.literal("该升级信息已被使用，请重新使用[shift + 右键]获取升级列表"));
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
                                                            level.sendParticles(ParticleTypes.ENCHANT,
                                                                    player.getX(), player.getY() + 1.0, player.getZ(),
                                                                    30, 0.5, 0.5, 0.5, 0.1);
                                                            level.playSound(null, player.blockPosition(),
                                                                    SoundEvents.ENCHANTMENT_TABLE_USE,
                                                                    SoundSource.PLAYERS, volume, 1.0F);
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
}