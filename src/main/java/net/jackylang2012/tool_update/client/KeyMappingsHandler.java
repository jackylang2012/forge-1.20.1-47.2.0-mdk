package net.jackylang2012.tool_update.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class KeyMappingsHandler {
    public static KeyMapping SHOW_ENCHANT_INFO;

    public static void register(RegisterKeyMappingsEvent event) {
        SHOW_ENCHANT_INFO = new KeyMapping(
                "key.tool_update.show_enchant_info",  // 显示的翻译键
                InputConstants.KEY_H,                 // 默认按键为 H
                "key.categories.tool_update"          // 分类
        );
        event.register(SHOW_ENCHANT_INFO);
    }

    public static String getKeyName() {
        return SHOW_ENCHANT_INFO.getTranslatedKeyMessage().getString();
    }
}
