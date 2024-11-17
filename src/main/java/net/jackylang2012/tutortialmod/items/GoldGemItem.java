package net.jackylang2012.tutortialmod.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GoldGemItem extends Item {
    public GoldGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack.isDamageableItem() && itemStack.getDamageValue() > 0) {
                    itemStack.setDamageValue(itemStack.getDamageValue() - 1); // 修复耐久值
                }
            }
        }
    }
}
