package net.jackylang2012.tutortialmod.datagen;

import net.jackylang2012.tutortialmod.LclMod;
import net.jackylang2012.tutortialmod.items.ModItems;
import net.jackylang2012.tutortialmod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> STAR_DREAM_SMELTABLES = List.of(ModItems.DREAMSTONE_HEART.get(),
            ModBlocks.DREAMSTONE_ORE_BLOCK.get(), ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK.get());
    private static final List<ItemLike> KUN_MEAT_SMELTABLES = List.of(ModItems.KUN_RAW_MEAT.get());
    private static final List<ItemLike> RAW_STAR_DREAM_SMELTABLES = List.of(ModItems.RAW_STAR_DREAM_INGOT.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreBlasting(pWriter, STAR_DREAM_SMELTABLES, RecipeCategory.MISC, ModItems.SIMPLE_STAR_DREAM_INGOT.get(), 0.25f, 100, "star_dream");
        oreSmelting(pWriter, STAR_DREAM_SMELTABLES, RecipeCategory.MISC, ModItems.SIMPLE_STAR_DREAM_INGOT.get(), 0.25f, 200, "star_dream");
        oreBlasting(pWriter, RAW_STAR_DREAM_SMELTABLES, RecipeCategory.MISC, ModItems.STAR_DREAM_INGOT.get(), 0.25f, 100, "star_dream");
        oreSmelting(pWriter, RAW_STAR_DREAM_SMELTABLES, RecipeCategory.MISC, ModItems.STAR_DREAM_INGOT.get(), 0.25f, 200, "star_dream");
        oreSmelting(pWriter, KUN_MEAT_SMELTABLES, RecipeCategory.MISC, ModItems.KUN_MEAT.get(), 0.25f, 200, "kun_meat");
        oreSmoking(pWriter, KUN_MEAT_SMELTABLES, RecipeCategory.MISC, ModItems.KUN_MEAT.get(), 0.35f, 100, "kun_meat");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_DREAM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.STAR_DREAM_INGOT.get())
                .unlockedBy(getHasName(ModItems.STAR_DREAM_INGOT.get()), has(ModItems.STAR_DREAM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RAW_STAR_DREAM_INGOT.get())
                .pattern("BAB")
                .pattern("ASA")
                .pattern("BAB")
                .define('S', Items.DIAMOND)
                .define('B', Items.GOLD_INGOT)
                .define('A', ModItems.SIMPLE_STAR_DREAM_INGOT.get())
                .unlockedBy(getHasName(ModItems.SIMPLE_STAR_DREAM_INGOT.get()), has(ModItems.SIMPLE_STAR_DREAM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUPER_SWORD_OF_THE_BRAVE.get())
                .pattern(" B ")
                .pattern(" B ")
                .pattern(" A ")
                .define('B', ModItems.STAR_DREAM_INGOT.get())
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy(getHasName(ModItems.STAR_DREAM_INGOT.get()), has(ModItems.STAR_DREAM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DREAM_PICKAXE.get())
                .pattern("BBB")
                .pattern(" A ")
                .pattern(" A ")
                .define('B', ModItems.STAR_DREAM_INGOT.get())
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy(getHasName(ModItems.STAR_DREAM_INGOT.get()), has(ModItems.STAR_DREAM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.KUN_COAL.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('B', ModItems.KUN_BLOOD.get())
                .define('A', Items.COAL)
                .unlockedBy(getHasName(ModItems.STAR_DREAM_INGOT.get()), has(ModItems.STAR_DREAM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_KUN_COAL.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('B', Items.DIAMOND)
                .define('A', ModItems.KUN_COAL.get())
                .unlockedBy(getHasName(ModItems.STAR_DREAM_INGOT.get()), has(ModItems.STAR_DREAM_INGOT.get()))
                .save(pWriter);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DREAM_FLINT.get(), 1)
                .requires(Items.FLINT)
                .requires(ModItems.STAR_DREAM_INGOT.get())
                .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                .save(pWriter);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STAR_DREAM_INGOT.get(), 9)
                .requires(ModBlocks.STAR_DREAM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.STAR_DREAM_BLOCK.get()), has(ModBlocks.STAR_DREAM_BLOCK.get()))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreSmoking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMOKING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, LclMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
