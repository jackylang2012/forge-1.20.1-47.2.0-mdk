package net.jackylang2012.tool_update.events;

import net.jackylang2012.tool_update.ToolProficiency;
import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ToolUsageEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ItemStack tool = player.getMainHandItem();
        Item item = tool.getItem();

        if (item instanceof PickaxeItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.PICKAXE_PROFICIENCY_PER_USE.get(), player);
        } else if (item instanceof AxeItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.AXE_PROFICIENCY_PER_USE.get(), player);
        } else if (item instanceof ShovelItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.SHOVEL_PROFICIENCY_PER_USE.get(), player);
        } else if (item instanceof ShearsItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.SHEARS_PROFICIENCY_PER_USE.get(), player);
        } else if (item instanceof FlintAndSteelItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.FLINT_AND_STEEL_PROFICIENCY_PER_USE.get(), player);
        }
    }

    @SubscribeEvent
    public static void onUseHoeOrShovel(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        BlockState state = event.getLevel().getBlockState(event.getPos());

        if (item instanceof HoeItem && item.canPerformAction(stack, ToolActions.HOE_TILL)) {
            if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {
                ToolProficiency.increaseProficiency(stack, ToolUpdateConfig.HOE_PROFICIENCY_PER_USE.get(), serverPlayer);
            }
        }

        if (item instanceof ShovelItem && item.canPerformAction(stack, ToolActions.SHOVEL_FLATTEN)) {
            if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {
                ToolProficiency.increaseProficiency(stack, ToolUpdateConfig.SHOVEL_PROFICIENCY_PER_USE.get(), serverPlayer);
            }
        }

        if (item instanceof FlintAndSteelItem) {
            ToolProficiency.increaseProficiency(stack, ToolUpdateConfig.FLINT_AND_STEEL_PROFICIENCY_PER_USE.get(), serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        ItemStack tool = player.getMainHandItem();
        Item item = tool.getItem();

        if (item instanceof SwordItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.SWORD_PROFICIENCY_PER_USE.get(), player);
        } else if (item instanceof AxeItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.AXE_PROFICIENCY_PER_USE.get(), player);
        } else if (item instanceof TridentItem) {
            ToolProficiency.increaseProficiency(tool, ToolUpdateConfig.TRIDENT_PROFICIENCY_PER_USE.get(), player);
        }
    }

    @SubscribeEvent
    public static void onFish(ItemFishedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack rod = player.getMainHandItem();
        if (rod.getItem() instanceof FishingRodItem) {
            ToolProficiency.increaseProficiency(rod, ToolUpdateConfig.FISHING_ROD_PROFICIENCY_PER_USE.get(), player);
        }
    }

    @SubscribeEvent
    public static void onSuccessfulBlock(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getAmount() > 0) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (mainHand.getItem() instanceof ShieldItem) {
            ToolProficiency.increaseProficiency(mainHand, ToolUpdateConfig.SHIELD_PROFICIENCY_PER_USE.get(), player);
        } else if (offHand.getItem() instanceof ShieldItem) {
            ToolProficiency.increaseProficiency(offHand, ToolUpdateConfig.SHIELD_PROFICIENCY_PER_USE.get(), player);
        }
    }

    @SubscribeEvent
    public static void onBrushUse(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof BrushItem)) return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock().getDescriptionId().contains("suspicious")) {
            ToolProficiency.increaseProficiency(stack, ToolUpdateConfig.BRUSH_PROFICIENCY_PER_USE.get(), serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onBowReleased(ArrowLooseEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ItemStack bow = event.getBow();
        if (!(bow.getItem() instanceof BowItem)) return;

        if (event.getCharge() >= 5) {
            ToolProficiency.increaseProficiency(bow, ToolUpdateConfig.BOW_PROFICIENCY_PER_USE.get(), serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onRangedAttack(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        ItemStack weapon = player.getMainHandItem();
        if (weapon.getItem() instanceof CrossbowItem) {
            ToolProficiency.increaseProficiency(weapon, ToolUpdateConfig.CROSSBOW_PROFICIENCY_PER_USE.get(), player);
        }
    }

    @SubscribeEvent
    public static void onTridentHit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        if ("trident".equals(event.getSource().getMsgId())) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TridentItem) {
                ToolProficiency.increaseProficiency(stack, ToolUpdateConfig.TRIDENT_PROFICIENCY_PER_USE.get(), player);
            }
        }
    }
}