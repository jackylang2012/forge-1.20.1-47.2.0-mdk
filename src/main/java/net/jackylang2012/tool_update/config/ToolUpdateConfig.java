package net.jackylang2012.tool_update.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ToolUpdateConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue BASE_COST;
    public static final ForgeConfigSpec.IntValue MAX_LEVEL_BONUS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_UPGRADE_EFFECTS;
    public static final ForgeConfigSpec.DoubleValue EFFECT_VOLUME;
    public static final ForgeConfigSpec.IntValue MAX_ENCHANT_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BLACKLISTED_ENCHANTS;
    public static final ForgeConfigSpec.BooleanValue SHOW_PROFICIENCY_TOOLTIP;

    public static final ForgeConfigSpec.IntValue SWORD_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue PICKAXE_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue AXE_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue SHOVEL_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue HOE_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue TRIDENT_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue FISHING_ROD_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue BOW_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue CROSSBOW_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue FLINT_AND_STEEL_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue SHEARS_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue SHIELD_PROFICIENCY_PER_USE;
    public static final ForgeConfigSpec.IntValue BRUSH_PROFICIENCY_PER_USE;

    static {
        BUILDER.comment("Tool Upgrade Configuration").push("general");

        BASE_COST = BUILDER
                .comment("Base cost per enchantment level")
                .defineInRange("baseCost", 100, 0, Integer.MAX_VALUE);

        MAX_LEVEL_BONUS = BUILDER
                .comment("Extra cost for exceeding max level")
                .defineInRange("maxLevelBonus", 1000, 0, Integer.MAX_VALUE);

        ENABLE_UPGRADE_EFFECTS = BUILDER
                .comment("Enable particle and sound effects on upgrade")
                .define("enableUpgradeEffects", true);

        EFFECT_VOLUME = BUILDER
                .comment("Volume of sound effects (0.0 - 1.0)")
                .defineInRange("effectVolume", 0.75, 0.0, 1.0);

        MAX_ENCHANT_LEVEL = BUILDER
                .comment("The max level of enchantments(1 - 114514)")
                .defineInRange("maxEnchantLevel", 15, 1, 114514);


        SHOW_PROFICIENCY_TOOLTIP = BUILDER
                .comment("Show tool proficiency in tooltip")
                .define("showProficiencyTooltip", true);

        // 黑名单配置 - 修改为直接存储字符串列表
        BLACKLISTED_ENCHANTS = BUILDER
                .comment("List of enchantment IDs that cannot be upgraded")
                .define("blacklistedEnchants",
                        getDefaultBlacklistedEnchants(),
                        list -> list instanceof List && ((List<?>)list).stream()
                                .allMatch(item -> item instanceof String));

// 在ToolUpdateConfig类中添加以下配置项

// 近战武器类 - 中等频率使用，中等收益
        SWORD_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用剑获得的熟练度 (建议: 2-5)")
                .defineInRange("swordProficiencyPerUse", 3, 0, Integer.MAX_VALUE);

        AXE_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用斧获得的熟练度 (建议: 2-5)")
                .defineInRange("axeProficiencyPerUse", 3, 0, Integer.MAX_VALUE);

        TRIDENT_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用三叉戟获得的熟练度 (建议: 5-10)")
                .defineInRange("tridentProficiencyPerUse", 8, 0, Integer.MAX_VALUE);

// 工具类 - 高频使用，低收益
        PICKAXE_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用镐获得的熟练度 (建议: 1-3)")
                .defineInRange("pickaxeProficiencyPerUse", 2, 0, Integer.MAX_VALUE);

        SHOVEL_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用锹获得的熟练度 (建议: 1-3)")
                .defineInRange("shovelProficiencyPerUse", 2, 0, Integer.MAX_VALUE);

        HOE_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用锄获得的熟练度 (建议: 1-3)")
                .defineInRange("hoeProficiencyPerUse", 2, 0, Integer.MAX_VALUE);

        SHEARS_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用剪刀获得的熟练度 (建议: 2-5)")
                .defineInRange("shearsProficiencyPerUse", 3, 0, Integer.MAX_VALUE);

        FLINT_AND_STEEL_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用打火石获得的熟练度 (建议: 3-8)")
                .defineInRange("flintAndSteelProficiencyPerUse", 5, 0, Integer.MAX_VALUE);

        BRUSH_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用刷子获得的熟练度 (建议: 10-20)")
                .defineInRange("brushProficiencyPerUse", 15, 0, Integer.MAX_VALUE);

// 防御类 - 特殊机制，中等收益
        SHIELD_PROFICIENCY_PER_USE = BUILDER
                .comment("每次成功格挡获得的熟练度 (建议: 5-15)")
                .defineInRange("shieldProficiencyPerUse", 10, 0, Integer.MAX_VALUE);

// 远程武器类 - 低频使用，高收益
        BOW_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用弓获得的熟练度 (建议: 5-15)")
                .defineInRange("bowProficiencyPerUse", 10, 0, Integer.MAX_VALUE);

        CROSSBOW_PROFICIENCY_PER_USE = BUILDER
                .comment("每次使用弩获得的熟练度 (建议: 10-20)")
                .defineInRange("crossbowProficiencyPerUse", 15, 0, Integer.MAX_VALUE);

// 特殊工具类 - 低频使用，高收益
        FISHING_ROD_PROFICIENCY_PER_USE = BUILDER
                .comment("每次成功钓鱼获得的熟练度 (建议: 5-15)")
                .defineInRange("fishingRodProficiencyPerUse", 10, 0, Integer.MAX_VALUE);



        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // 工具熟练度获取方法
    public static int getBaseProficiencyForTool(String toolType) {
        return switch (toolType.toLowerCase()) {
            case "sword" -> SWORD_PROFICIENCY_PER_USE.get();
            case "pickaxe" -> PICKAXE_PROFICIENCY_PER_USE.get();
            case "axe" -> AXE_PROFICIENCY_PER_USE.get();
            case "shovel" -> SHOVEL_PROFICIENCY_PER_USE.get();
            case "hoe" -> HOE_PROFICIENCY_PER_USE.get();
            case "trident" -> TRIDENT_PROFICIENCY_PER_USE.get();
            case "fishing_rod" -> FISHING_ROD_PROFICIENCY_PER_USE.get();
            case "bow" -> BOW_PROFICIENCY_PER_USE.get();
            case "crossbow" -> CROSSBOW_PROFICIENCY_PER_USE.get();
            case "flint_and_steel" -> FLINT_AND_STEEL_PROFICIENCY_PER_USE.get();
            case "shears" -> SHEARS_PROFICIENCY_PER_USE.get();
            case "shield" -> SHIELD_PROFICIENCY_PER_USE.get();
            case "brush" -> BRUSH_PROFICIENCY_PER_USE.get();
            default -> 1;
        };
    }

    // 获取默认黑名单
    public static List<String> getDefaultBlacklistedEnchants() {
        List<String> defaultEnchants = new ArrayList<>();
        // 硬编码的基础黑名单
        defaultEnchants.add("minecraft:aqua_affinity");
        defaultEnchants.add("minecraft:binding_curse");
        defaultEnchants.add("minecraft:vanishing_curse");
        defaultEnchants.add("minecraft:silk_touch");
        defaultEnchants.add("minecraft:flame");
        defaultEnchants.add("minecraft:infinity");
        defaultEnchants.add("minecraft:channeling");
        defaultEnchants.add("minecraft:multishot");
        defaultEnchants.add("minecraft:mending");

        // 动态添加原版最大等级为1的附魔
        BuiltInRegistries.ENCHANTMENT.stream()
                .filter(enchant -> enchant.getMaxLevel() <= 1)
                .forEach(enchant -> {
                    String id = BuiltInRegistries.ENCHANTMENT.getKey(enchant).toString();
                    if (!defaultEnchants.contains(id)) {
                        defaultEnchants.add(id);
                    }
                });

        return defaultEnchants;
    }

    // 添加方法确保黑名单值有效
    public static List<String> getValidBlacklistedEnchants() {
        // 使用通配符捕获转换
        List<? extends String> current = BLACKLISTED_ENCHANTS.get();
        List<String> valid = new ArrayList<>();

        for (String enchantId : current) {
            if (enchantId != null && !enchantId.trim().isEmpty()) {
                valid.add(enchantId.trim());
            }
        }

        return valid;
    }

    private List<String> validateBlacklist(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> s.matches("^[a-z0-9_:-]+$")) // 基本格式验证
                .collect(Collectors.toList());
    }

    // 从字符串解析黑名单(用于UI输入)
    public static List<String> parseBlacklistFromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(ToolUpdateConfig::isValidEnchantmentId)
                .collect(Collectors.toList());
    }

    // 验证黑名单格式
    public static boolean isValidEnchantmentId(String id) {
        return id != null && id.matches("^[a-z0-9_:-]+$");
    }

    // 合并黑名单到单个字符串(用于UI显示)
    public static String getBlacklistAsString() {
        return String.join(", ", getCurrentBlacklist());
    }

    // 安全设置黑名单
    public static void setBlacklist(List<String> enchants) {
        BLACKLISTED_ENCHANTS.set(enchants.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(String::trim)
                .collect(Collectors.toList()));
    }

    // 获取当前有效的黑名单
    public static List<String> getCurrentBlacklist() {
        return new ArrayList<>(BLACKLISTED_ENCHANTS.get());
    }
}
