package net.jackylang2012.tutortialmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PlantGemItem extends Item {
    public PlantGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            // 检查玩家是否持有植物宝石并且是否在蹲下
            if ((player.getMainHandItem() == stack || player.getOffhandItem() == stack) && player.isCrouching()) {
                BlockPos playerPos = player.blockPosition();

                // 处理周围 3x3 范围内的作物催熟
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos targetPos = playerPos.offset(x, 0, z);
                        BlockState blockState = level.getBlockState(targetPos);

                        // 如果是小麦，胡萝卜，土豆，或甜菜，且没有成熟，催熟作物
                        if (blockState.getBlock() == Blocks.WHEAT && blockState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7) < 7) {
                            level.setBlock(targetPos, Blocks.WHEAT.defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7, 7), 2); // 催熟小麦
                        } else if (blockState.getBlock() == Blocks.CARROTS && blockState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7) < 7) {
                            level.setBlock(targetPos, Blocks.CARROTS.defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7, 7), 2); // 催熟胡萝卜
                        } else if (blockState.getBlock() == Blocks.POTATOES && blockState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7) < 7) {
                            level.setBlock(targetPos, Blocks.POTATOES.defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_7, 7), 2); // 催熟土豆
                        } else if (blockState.getBlock() == Blocks.BEETROOTS && blockState.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_3) < 3) {
                            level.setBlock(targetPos, Blocks.BEETROOTS.defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_3, 3), 2); // 催熟甜菜
                        }


                    }
                }
            }
        }
    }
}
