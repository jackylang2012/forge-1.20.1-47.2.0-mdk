package net.jackylang2012.tool_update;

import net.jackylang2012.tool_update.commands.ModCommands;
import net.jackylang2012.tool_update.config.ToolUpdateConfig;
import net.jackylang2012.tool_update.network.ModMessages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(ToolUpdate.MOD_ID)
public class ToolUpdate {
    public static final String MOD_ID = "tool_update";

    public ToolUpdate() {
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ToolUpdateConfig.SPEC);
        ModMessages.register();

    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }



}
