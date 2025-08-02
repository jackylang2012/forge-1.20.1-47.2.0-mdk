package net.jackylang2012.tool_update;

import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class TooltipEvents {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.get()) return;

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        int proficiency = ToolProficiency.getProficiency(stack);
        if (proficiency > 0) {
            Component proficiencyLine = Component.literal("熟练度: " + proficiency)
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
            event.getToolTip().add(proficiencyLine);
        }
    }
}