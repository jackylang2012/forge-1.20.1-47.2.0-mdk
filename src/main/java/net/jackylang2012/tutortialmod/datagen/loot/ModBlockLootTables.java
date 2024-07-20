package net.jackylang2012.tutortialmod.datagen.loot;

import net.jackylang2012.tutortialmod.items.Moditems;
import net.jackylang2012.tutortialmod.items.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.STAR_DREAM_BLOCK.get());
        this.add(ModBlocks.DREAMSTONE_ORE_BLOCK.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DREAMSTONE_ORE_BLOCK.get(), Moditems.STAR_DREAM_INGOT.get()));
        this.add(ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK.get(),
                block -> createCopperLikeOreDrops(ModBlocks.DEEPSLATE_DREAMSTONE_ORE_BLOCK.get(), Moditems.STAR_DREAM_INGOT.get()));

    }
    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
