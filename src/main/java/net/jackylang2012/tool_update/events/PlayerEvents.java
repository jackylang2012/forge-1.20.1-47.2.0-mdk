package net.jackylang2012.tool_update.events;

import net.jackylang2012.tool_update.ToolProficiency;
import net.jackylang2012.tool_update.util.EnchantUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (player.isShiftKeyDown() && !stack.isEmpty() && !EnchantmentHelper.getEnchantments(stack).isEmpty()) {
            int proficiency = ToolProficiency.getProficiency(stack);
            player.sendSystemMessage(EnchantUtils.createEnchantList(stack, proficiency));
            event.setCanceled(true);
        }
    }
}
