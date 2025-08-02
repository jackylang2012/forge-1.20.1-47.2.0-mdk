package net.jackylang2012.tool_update;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ToolProficiency {
    private static final String NBT_KEY = "tool_update_proficiency";

    // 获取熟练度
    public static int getProficiency(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt(NBT_KEY);
    }

    // 设置熟练度（不允许负数）
    public static void setProficiency(ItemStack stack, int value) {
        if (stack.isEmpty()) return;
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(NBT_KEY, Math.max(0, value));
    }

    // 增加熟练度
    public static void increaseProficiency(ItemStack stack, int amount) {
        int oldValue = getProficiency(stack);
        setProficiency(stack, oldValue + amount);
    }

    // 减少熟练度，返回是否成功
    public static boolean decreaseProficiency(ItemStack stack, int amount) {
        int oldValue = getProficiency(stack);
        if (oldValue < amount) return false;
        setProficiency(stack, oldValue - amount);
        return true;
    }
}
