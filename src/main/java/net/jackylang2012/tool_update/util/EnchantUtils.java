package net.jackylang2012.tool_update.util;

import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EnchantUtils {
    public static Component createEnchantList(ItemStack stack, int proficiency) {
        if (stack.isEmpty()) {
            return Component.literal("无效的物品").withStyle(ChatFormatting.RED);
        }

        if (!stack.isEnchanted()) {
            return Component.literal("该物品没有附魔").withStyle(ChatFormatting.YELLOW);
        }

        MutableComponent message = Component.literal("=== 可升级附魔 ===\n")
                .append(Component.literal("可用熟练度: " + proficiency + "\n").withStyle(ChatFormatting.GOLD))
                .withStyle(ChatFormatting.BOLD);

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.isEmpty()) {
            return message.append(Component.literal("无有效附魔").withStyle(ChatFormatting.GRAY));
        }

        enchantments.entrySet().stream()
                .sorted(Map.Entry.comparingByKey((e1, e2) ->
                        e1.getFullname(1).getString().compareToIgnoreCase(e2.getFullname(1).getString())))
                .forEach(entry -> {
                    Enchantment enchant = entry.getKey();
                    int level = entry.getValue();
                    int nextLevel = level + 1;
                    int cost = calculateUpgradeCost(enchant, level);
                    ResourceLocation id = BuiltInRegistries.ENCHANTMENT.getKey(enchant);
                    if (id != null) {
                        message.append(createEnchantLine(enchant, id, level, nextLevel, cost, proficiency));
                    }
                });

        return message;
    }

    private static int calculateUpgradeCost(Enchantment enchant, int level) {
        return ToolUpdateConfig.BASE_COST.get() * level
                + (enchant.getRarity().getWeight() * 20)
                + (level >= enchant.getMaxLevel() ? ToolUpdateConfig.MAX_LEVEL_BONUS.get() : 0);
    }

    private static MutableComponent createEnchantLine(
            Enchantment enchant,
            ResourceLocation enchantId,
            int level,
            int nextLevel,
            int cost,
            int proficiency
    ) {
        boolean canUpgrade = proficiency >= cost;
        boolean isMaxLevel = level >= 15;
        ChatFormatting color = canUpgrade ? ChatFormatting.GREEN : ChatFormatting.RED;

        MutableComponent line = Component.literal(" • ")
                .append(enchant.getFullname(level))
                .append(" → ")
                .append(isMaxLevel
                        ? Component.literal("MAX").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD)
                        : Component.literal(String.valueOf(nextLevel)).withStyle(ChatFormatting.YELLOW))
                .append(" (消耗: ")
                .append(Component.literal(String.valueOf(cost)).withStyle(color))
                .append(")")
                .append("\n  ")
                .append(Component.literal(isMaxLevel ? "已达到最大等级" : (canUpgrade ? "可升级" : "熟练度不足"))
                        .withStyle(isMaxLevel ? ChatFormatting.DARK_PURPLE : color))
                .append("\n");

        if (!isMaxLevel && canUpgrade && !ToolUpdateConfig.BLACKLISTED_ENCHANTS.get().contains(enchantId.toString())) {
            String enchantIdStr = "\"" + enchantId.toString() + "\"";
            String cmd = String.format("/tool_update upgrade %s %d %d", enchantIdStr, cost, proficiency);

            line = line.withStyle(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            createHoverText(enchant, level, nextLevel, cost, isMaxLevel)
                    )));
        } else {
            line = line.withStyle(style -> style
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            createHoverText(enchant, level, nextLevel, cost, isMaxLevel)
                    )));
        }

        return line;
    }

    private static Component createHoverText(
            Enchantment enchant,
            int currentLevel,
            int nextLevel,
            int cost,
            boolean isMaxLevel
    ) {
        MutableComponent hover = Component.literal("附魔: ")
                .append(enchant.getFullname(currentLevel))
                .append("\n当前等级: ")
                .append(Component.literal(String.valueOf(currentLevel)).withStyle(ChatFormatting.YELLOW));

        if (!isMaxLevel) {
            hover.append("\n下一等级: ")
                    .append(Component.literal(String.valueOf(nextLevel)).withStyle(ChatFormatting.YELLOW))
                    .append("\n消耗熟练度: ")
                    .append(Component.literal(String.valueOf(cost)).withStyle(ChatFormatting.GOLD));
        } else {
            hover.append("\n").append(Component.literal("已达到最大等级").withStyle(ChatFormatting.DARK_PURPLE));
        }

        return hover.append("\n\n点击升级").withStyle(ChatFormatting.GRAY);
    }
}