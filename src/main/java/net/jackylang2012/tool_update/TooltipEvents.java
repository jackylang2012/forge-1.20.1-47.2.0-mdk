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
            // 创建更丰富的工具提示
            Component proficiencyLine = Component.literal("✦ ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.translatable("tooltip.proficiency", proficiency)
                            .withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(" ✦")
                            .withStyle(ChatFormatting.GOLD));

            event.getToolTip().add(proficiencyLine);
        }
    }
}