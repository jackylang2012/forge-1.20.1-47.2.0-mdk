package net.jackylang2012.tutortialmod.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FireGemItem extends Item {

    public FireGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            // 检查玩家背包中是否有 FireGemItem
            boolean hasFireGem = false;
            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack.getItem() instanceof FireGemItem) {
                    hasFireGem = true;
                    break;
                }
            }

            // 如果玩家的背包中有 FireGemItem，给玩家添加火焰抗性效果
            if (hasFireGem) {
                if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, false, false));
                }
            } else {
                // 如果背包中没有 FireGemItem，移除火焰抗性效果（如果有）
                if (player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    player.removeEffect(MobEffects.FIRE_RESISTANCE);
                }
            }
        }
    }
}
