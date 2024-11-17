package net.jackylang2012.tutortialmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class IceGemItem extends Item {

    public IceGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // 获取玩家和所在的世界
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (player != null && !level.isClientSide()) {
            // 获取玩家当前所在的位置
            BlockPos playerPos = player.blockPosition();

            // 获取玩家周围半径5个方块范围内的敌对生物
            List<Monster> monsters = level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(5), monster -> monster.getTarget() == null);

            // 对周围的每个敌对生物应用冻结效果
            for (Monster monster : monsters) {
                // 将敌对生物的当前位置变为冰块
                BlockPos monsterPos = monster.blockPosition();
                level.setBlock(monsterPos, Blocks.ICE.defaultBlockState(), 3);

                // 使敌人被冻结，无法移动
                freezeEntity(monster);

                // 15秒后解除冻结
                scheduleUnfreeze(level, monster);
            }
        }

        return InteractionResult.SUCCESS;
    }

    // 使敌对生物被冻结，无法移动
    private void freezeEntity(Monster monster) {
        // 给敌人添加一个冻结效果，让它不能移动
        monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 255, false, false)); // 让敌人无法移动，持续5秒（300 ticks）
        monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 1, false, false));
        monster.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 1, false, false));
    }


    // 在15秒后解除冻结效果
    private void scheduleUnfreeze(Level level, Monster monster) {
        // 安排在15秒（300 ticks）后解除冻结
        level.scheduleTick(monster.blockPosition(), Blocks.AIR, 300);
    }

    // 解除敌对生物的冻结
    public static void unfreezeEntity(Level level, Monster monster) {
        // 解除冻结效果，使敌人恢复正常
        if (monster.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            monster.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
    }
}
