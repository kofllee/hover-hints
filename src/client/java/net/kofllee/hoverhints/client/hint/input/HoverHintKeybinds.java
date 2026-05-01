package net.kofllee.hoverhints.client.hint.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class HoverHintKeybinds {
    public static KeyBinding SHOW_HINTS;

    private HoverHintKeybinds() {}

    public static void register() {
        SHOW_HINTS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hoverhints.show_hints",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.hoverhints"
        ));
    }

    public static boolean isHintModeActive(){
        return SHOW_HINTS != null && SHOW_HINTS.isPressed();
    }
}
