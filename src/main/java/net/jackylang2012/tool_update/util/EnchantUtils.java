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
    public static Component createEnchantList(ItemStack tool, int proficiency) {
        MutableComponent message = Component.empty();

        // 标题 - 金色标题，青色熟练度
        message.append(Component.translatable("gui.enchant_list.title", proficiency))
                .append("\n\n");

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(tool);

        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchant = entry.getKey();
            int currentLevel = entry.getValue();
            int maxLevel = enchant.getMaxLevel();
            String currentLevelStr = getRomanNumeral(currentLevel);
            String enchantName = enchant.getFullname(currentLevel).getString().replace(currentLevelStr, "").trim();

            if (currentLevel < maxLevel) {
                int cost = calculateUpgradeCost(enchant, currentLevel);
                String nextLevel = getRomanNumeral(currentLevel + 1);

                // 根据等级范围分配颜色
                ChatFormatting levelColor = getLevelColor(currentLevel);
                ChatFormatting nextLevelColor = getLevelColor(currentLevel + 1);

                // 构建彩色升级行
                MutableComponent upgradeLine = Component.translatable("gui.enchant_list.upgrade_bracket") // 绿色[
                        .append(Component.literal(enchantName) // 附魔名称（亮粉色）
                                .withStyle(ChatFormatting.LIGHT_PURPLE))
                        .append(Component.literal(" " + currentLevelStr) // 当前等级（根据等级颜色）
                                .withStyle(levelColor))
                        .append(Component.translatable("gui.enchant_list.upgrade_arrow")) // 白色→
                        .append(Component.literal(" " + nextLevel) // 下一等级（根据等级颜色）
                                .withStyle(nextLevelColor))
                        .append(Component.translatable("gui.enchant_list.upgrade_cost", cost)) // 灰色消耗，蓝色←，青色点击
                        .withStyle(style -> style
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        createUpgradeCommand(enchant, cost, proficiency)))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.translatable("tooltip.click_to_upgrade")
                                                .withStyle(ChatFormatting.AQUA))));

                message.append(upgradeLine).append("\n");
            } else {
                // 构建最大等级行 - 特殊提示
                MutableComponent maxLevelLine = Component.translatable("gui.enchant_list.max_level_bracket") // 金色[
                        .append(Component.literal(enchantName) // 附魔名称（亮粉色）
                                .withStyle(ChatFormatting.LIGHT_PURPLE))
                        .append(Component.literal(" " + currentLevelStr) // 当前等级（彩虹色表示最大）
                                .withStyle(getMaxLevelColor()))
                        .append(Component.translatable("gui.enchant_list.max_level_text")) // 特殊最大等级提示
                        .withStyle(ChatFormatting.GOLD); // 金色提示文字

                message.append(maxLevelLine).append("\n");
            }
        }

        return message;
    }

    // 根据等级范围分配颜色
    private static ChatFormatting getLevelColor(int level) {
        if (level <= 2) {
            return ChatFormatting.GRAY; // 1-2级：灰色（初级）
        } else if (level <= 4) {
            return ChatFormatting.WHITE; // 3-4级：白色（中级）
        } else if (level <= 6) {
            return ChatFormatting.YELLOW; // 5-6级：黄色（高级）
        } else if (level <= 8) {
            return ChatFormatting.GOLD; // 7-8级：金色（专家级）
        } else if (level <= 10) {
            return ChatFormatting.AQUA; // 9-10级：青色（大师级）
        } else {
            return ChatFormatting.LIGHT_PURPLE; // 10级以上：粉紫色（传奇级）
        }
    }

    // 最大等级的特殊颜色（彩虹效果）
    private static Style getMaxLevelColor() {
        return Style.EMPTY
                .withColor(TextColor.fromRgb(0xFF0000)) // 红色
                .withBold(true);
    }

    // 为最大等级添加特殊提示文字
    private static MutableComponent getMaxLevelText() {
        return Component.literal("] ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal("★ MAX LEVEL ★")
                        .withStyle(Style.EMPTY
                                .withColor(TextColor.fromRgb(0xFFD700)) // 金色
                                .withBold(true)))
                .append(Component.literal(" - ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("无法继续升级")
                        .withStyle(ChatFormatting.DARK_GRAY));
    }

    // 罗马数字转换工具方法（算法版，支持 1 - 100）
    private static String getRomanNumeral(int number) {
        if (number <= 0 || number > 100) {
            return String.valueOf(number); // 超出范围就直接返回数字
        }

        int[] values = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                sb.append(symbols[i]);
            }
        }
        return sb.toString();
    }

    private static String createUpgradeCommand(Enchantment enchant, int cost, int proficiency) {
        ResourceLocation enchantId = BuiltInRegistries.ENCHANTMENT.getKey(enchant);
        return String.format("/tool_update upgrade %s %d %d",
                enchantId.toString(), cost, proficiency);
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
        boolean isBlacklisted = ToolUpdateConfig.BLACKLISTED_ENCHANTS.get().contains(enchantId.toString());
        boolean isMaxLevel = level >= ToolUpdateConfig.MAX_ENCHANT_LEVEL.get();
        boolean canUpgrade = !isBlacklisted && !isMaxLevel && proficiency >= cost;

        // 状态判断优先级：黑名单 > 最大等级 > 可升级/熟练度不足
        ChatFormatting color;
        String statusText;

        if (isBlacklisted) {
            color = ChatFormatting.RED;
            statusText = "该附魔已被禁用升级";
        } else if (isMaxLevel) {
            color = ChatFormatting.DARK_PURPLE;
            statusText = "已达到最大等级";
        } else if (canUpgrade) {
            color = ChatFormatting.GREEN;
            statusText = "可升级";
        } else {
            color = ChatFormatting.RED;
            statusText = "熟练度不足";
        }

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
                .append(Component.literal(statusText).withStyle(color))
                .append("\n");

        // 添加悬浮提示
        line = line.withStyle(style -> style
                .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        createHoverText(enchant, level, nextLevel, cost, isMaxLevel, isBlacklisted)
                )));

        // 只有可升级的附魔才添加点击事件
        if (canUpgrade) {
            String enchantIdStr = "\"" + enchantId.toString() + "\"";
            String cmd = String.format("/tool_update upgrade %s %d %d", enchantIdStr, cost, proficiency);
            line = line.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd)));
        }

        return line;
    }

    private static Component getStatusText(boolean isMaxLevel, boolean isBlacklisted, boolean canUpgrade, ChatFormatting color) {
        if (isMaxLevel) {
            return Component.literal("已达到最大等级").withStyle(ChatFormatting.DARK_PURPLE);
        } else if (isBlacklisted) {
            return Component.literal("该附魔已被禁用升级").withStyle(ChatFormatting.RED);
        } else if (canUpgrade) {
            return Component.literal("可升级").withStyle(ChatFormatting.GREEN);
        } else {
            return Component.literal("熟练度不足").withStyle(ChatFormatting.RED);
        }
    }

    private static Component createHoverText(
            Enchantment enchant,
            int currentLevel,
            int nextLevel,
            int cost,
            boolean isMaxLevel,
            boolean isBlacklisted
    ) {
        MutableComponent hover = Component.literal("附魔: ")
                .append(enchant.getFullname(currentLevel))
                .append("\n当前等级: ")
                .append(Component.literal(String.valueOf(currentLevel)).withStyle(ChatFormatting.YELLOW));

        if (isMaxLevel) {
            hover.append("\n").append(Component.literal("已达到最大等级").withStyle(ChatFormatting.DARK_PURPLE));
        } else if (isBlacklisted) {
            hover.append("\n").append(Component.literal("该附魔已被禁用升级").withStyle(ChatFormatting.RED));
        } else {
            hover.append("\n下一等级: ")
                    .append(Component.literal(String.valueOf(nextLevel)).withStyle(ChatFormatting.YELLOW))
                    .append("\n消耗熟练度: ")
                    .append(Component.literal(String.valueOf(cost)).withStyle(ChatFormatting.GOLD));
        }

        if (!isMaxLevel && !isBlacklisted) {
            hover.append("\n\n点击升级").withStyle(ChatFormatting.GRAY);
        }

        return hover;
    }
}