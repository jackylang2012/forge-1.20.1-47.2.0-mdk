package net.jackylang2012.tool_update.events;

import net.jackylang2012.tool_update.ToolProficiency;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ToolUsageEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ItemStack tool = player.getMainHandItem();
        if (tool.getItem() instanceof TieredItem) {
            ToolProficiency.increaseProficiency(tool, 1);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        ItemStack tool = player.getMainHandItem();
        if (tool.getItem() instanceof SwordItem || tool.getItem() instanceof AxeItem) {
            ToolProficiency.increaseProficiency(tool, 1);
        }
    }

    @SubscribeEvent
    public static void onFish(ItemFishedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack rod = player.getMainHandItem();
        if (rod.getItem() instanceof FishingRodItem) {
            ToolProficiency.increaseProficiency(rod, 1);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof BowItem
                || stack.getItem() instanceof CrossbowItem
                || stack.getItem() instanceof TridentItem) {
            if (player instanceof ServerPlayer) {
                ToolProficiency.increaseProficiency(stack, 1);
            }
        }
    }
}
