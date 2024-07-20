package net.jackylang2012.tutortialmod.datagen;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.datagen.loot.ModBlockLootTables;
import net.jackylang2012.tutortialmod.items.Moditems;
import net.jackylang2012.tutortialmod.items.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider  extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> STAR_DREAM_SMELTABLES = List.of(Moditems.RAW_STAR_DREAM_INGOT.get(),
            ModBlocks.DREAMSTONE_ORE_BLOCK.get(), ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK.get());
    private static final List<ItemLike> KUN_MEAT_SMELTABLES = List.of(Moditems.KUN_RAW_MEAT.get());
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }



    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreBlasting(pWriter, STAR_DREAM_SMELTABLES, RecipeCategory.MISC, Moditems.STAR_DREAM_INGOT.get(), 0.25f, 100, "star_dream");
        oreSmelting(pWriter, STAR_DREAM_SMELTABLES, RecipeCategory.MISC, Moditems.STAR_DREAM_INGOT.get(), 0.25f, 200, "star_dream");
        oreSmelting(pWriter, KUN_MEAT_SMELTABLES, RecipeCategory.MISC, Moditems.KUN_MEAT.get(), 0.25f, 200, "star_dream");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_DREAM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', Moditems.STAR_DREAM_INGOT.get())
                .unlockedBy(getHasName(Moditems.STAR_DREAM_INGOT.get()), has(Moditems.STAR_DREAM_INGOT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Moditems.STAR_DREAM_INGOT.get(), 9)
                .requires(ModBlocks.STAR_DREAM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.STAR_DREAM_BLOCK.get()), has(ModBlocks.STAR_DREAM_BLOCK.get()))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult
                            , pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, LclMod.MOD_ID + getItemName(pResult) + ":" + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
