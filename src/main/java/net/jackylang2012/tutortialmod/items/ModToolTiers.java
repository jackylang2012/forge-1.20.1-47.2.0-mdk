package net.jackylang2012.tutortialmod.items;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier DREAM = TierSortingRegistry.registerTier(
            new ForgeTier(10, 5883, 16.0f, 10.0f, 30,
                    ModTags.Blocks.NEED_DREAM_TOOL, () -> Ingredient.of(ModItems.STAR_DREAM_INGOT.get())),
            new ResourceLocation(LclMod.MOD_ID, "star_dream"), List.of(Tiers.NETHERITE), List.of());
    ;
    public static final Tier BRAVE = TierSortingRegistry.registerTier(
            new ForgeTier(15, 11766, 18.0f, 15.0f, 50,
                    ModTags.Blocks.NEED_BRAVE_TOOL, () -> Ingredient.of(ModItems.STAR_DREAM_INGOT.get())),
            new ResourceLocation(LclMod.MOD_ID, "brave"), List.of(Tiers.NETHERITE), List.of());
    ;
}

