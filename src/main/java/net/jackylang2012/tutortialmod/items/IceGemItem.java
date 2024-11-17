package net.jackylang2012.tutortialmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.jackylang2012.tutortialmod.block.ModBlocks;
import net.minecraft.world.item.ItemCooldowns;

import java.util.List;

public class IceGemItem extends Item {

    // 冷却时间（300 ticks = 15秒）
    private static final int COOLDOWN_TIME = 300;

    public IceGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (player != null && !level.isClientSide()) {
            ItemStack stack = context.getItemInHand();

            // 获取玩家的冷却系统
            ItemCooldowns cooldowns = player.getCooldowns();

            // 如果物品已经在冷却中，阻止再次使用
            if (cooldowns.isOnCooldown(stack.getItem())) {
                return InteractionResult.FAIL; // 不执行技能效果
            }

            // 设置物品冷却
            cooldowns.addCooldown(stack.getItem(), COOLDOWN_TIME);

            // 获取玩家当前所在的位置
            BlockPos playerPos = player.blockPosition();

            // 获取玩家周围半径5个方块范围内的敌对生物
            List<Monster> monsters = level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(5), monster -> monster.getTarget() == null);

            // 对周围的每个敌对生物应用冻结效果
            for (Monster monster : monsters) {
                // 获取敌对生物的当前位置
                BlockPos monsterPos = monster.blockPosition();

                // 将敌对生物的当前位置设置为 STRANGE_BLUE_ICE（奇异蓝冰）
                level.setBlock(monsterPos, ModBlocks.STRANGE_BLUE_ICE.get().defaultBlockState(), 3);

                // 使敌人被冻结，无法移动
                freezeEntity(monster);

                // 15秒后解除冻结，并恢复为原状态
                scheduleUnfreeze(level, monster, monsterPos);
            }
        }

        return InteractionResult.SUCCESS;
    }

    // 使敌对生物被冻结，无法移动
    private void freezeEntity(Monster monster) {
        // 给敌人添加一个冻结效果，让它不能移动
        monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 255, false, false)); // 让敌人无法移动，持续5秒（300 ticks）
        monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 3, false, true));
        monster.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 2, false, true));
    }

    // 在15秒后解除冻结效果，并恢复为原状态
    private void scheduleUnfreeze(Level level, Monster monster, BlockPos originalPos) {
        // 安排在15秒（300 ticks）后解除冻结
        level.scheduleTick(monster.blockPosition(), Blocks.AIR, 300);

        // 15秒后恢复原状（替换冰块为空气）
        level.scheduleTick(originalPos, Blocks.AIR, 300);
    }

    // 解除敌对生物的冻结
    public static void unfreezeEntity(Level level, Monster monster) {
        // 解除冻结效果，使敌人恢复正常
        if (monster.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            monster.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
    }
}
