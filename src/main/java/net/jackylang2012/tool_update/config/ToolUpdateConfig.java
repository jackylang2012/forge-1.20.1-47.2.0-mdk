package net.jackylang2012.tool_update.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ToolUpdateConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue BASE_COST;
    public static final ForgeConfigSpec.IntValue MAX_LEVEL_BONUS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_UPGRADE_EFFECTS;
    public static final ForgeConfigSpec.DoubleValue EFFECT_VOLUME;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_ENCHANTS;
    public static final ForgeConfigSpec.BooleanValue SHOW_PROFICIENCY_TOOLTIP;

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

        SHOW_PROFICIENCY_TOOLTIP = BUILDER
                .comment("Show tool proficiency in tooltip")
                .define("showProficiencyTooltip", true);

        BLACKLISTED_ENCHANTS = BUILDER
                .comment("List of enchantment IDs that cannot be upgraded")
                .defineListAllowEmpty(
                        "blacklistedEnchants",  // 键名
                        List.of("minecraft:vanishing_curse", "minecraft:binding_curse"),  // 默认值
                        obj -> obj instanceof String
                );


        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
