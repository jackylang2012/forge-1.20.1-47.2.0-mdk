package net.jackylang2012.tutortialmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class WaterGemItem extends Item {
    public WaterGemItem(Properties properties) {
        super(properties);
    }

    // 右键生成水源方块并给予玩家效果
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

        if (!level.isClientSide() && level.getBlockState(pos).isAir()) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3); // 放置水源方块
        }
        return InteractionResult.SUCCESS;
    }

    // 玩家背包中存在时提供效果
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            // 检查玩家背包是否包含此物品
            boolean hasWaterGem = player.getInventory().items.stream()
                    .anyMatch(itemStack -> itemStack.getItem() instanceof WaterGemItem);

            if (hasWaterGem) {
                // 添加“海豚的恩惠”效果，持续时间为1秒
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 20, 0, true, false, false));
            }
        }
    }
}
