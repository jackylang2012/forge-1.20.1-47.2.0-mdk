package net.jackylang2012.tool_update.util;

public class KeybindManager {
    private static String clientKeybind = "H"; // 默认值

    public static void setClientKeybind(String keybind) {
        clientKeybind = keybind;
    }

    public static String getKeybind() {
        return clientKeybind;
    }
}