package net.jackylang2012.tool_update.network;

import net.jackylang2012.tool_update.util.KeybindManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KeybindPacket {
    private final String keyName;

    public KeybindPacket(String keyName) {
        this.keyName = keyName;
    }

    public static void encode(KeybindPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.keyName);
    }

    public static KeybindPacket decode(FriendlyByteBuf buffer) {
        return new KeybindPacket(buffer.readUtf());
    }

    public static void handle(KeybindPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 这里可以存储接收到的按键名称
            KeybindManager.setClientKeybind(msg.keyName);
        });
        ctx.get().setPacketHandled(true);
    }
}