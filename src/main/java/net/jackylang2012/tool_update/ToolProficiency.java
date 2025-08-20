package net.jackylang2012.tool_update;

import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.jackylang2012.tool_update.util.EnchantUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolProficiency {
    private static final String NBT_KEY = "tool_update_proficiency";

    // 升级阈值
    private static final int PROFICIENCY_THRESHOLD = ToolUpdateConfig.BASE_COST.get();

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

    /**
     * 增加熟练度，并检测是否达到升级阈值，达到时发送聊天提示
     * @param stack 要操作的工具
     * @param amount 增加的熟练度数量
     * @param player 当前玩家，用于发送升级提示
     */
    public static void increaseProficiency(ItemStack stack, int amount, ServerPlayer player) {
        if (stack.isEmpty()) return;

        int oldValue = getProficiency(stack);
        int newValue = oldValue + amount;
        setProficiency(stack, newValue);

    }

    // 减少熟练度，返回是否成功
    public static boolean decreaseProficiency(ItemStack stack, int amount) {
        int oldValue = getProficiency(stack);
        if (oldValue < amount) return false;
        setProficiency(stack, oldValue - amount);
        return true;
    }

}
