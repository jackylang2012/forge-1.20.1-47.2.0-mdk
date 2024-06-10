package net.jackylang2012.tutortialmod.items;

import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties KUN_MEAT = new FoodProperties.Builder().meat().nutrition(32)
            .saturationMod(0.5f).effect(()-> new MobEffectInstance(MobEffects.REGENERATION, 5000), 1f)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2500), 1f)
            .effect(()-> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2500), 1f)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION, 2500), 1f).alwaysEat().build();
    public static final FoodProperties KUN_RAW_MEAT = new FoodProperties.Builder().meat().nutrition(5).saturationMod(0.5f).build();
}
