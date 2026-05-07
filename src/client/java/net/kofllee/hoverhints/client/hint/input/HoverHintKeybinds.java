package net.kofllee.hoverhints.client.hint.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kofllee.hoverhints.client.config.HintActivationMode;
import net.kofllee.hoverhints.client.config.HoverHintsConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class HoverHintKeybinds {
    public static KeyBinding SHOW_HINTS;

    private static final KeyBinding.Category CATEGORY =
            KeyBinding.Category.create(Identifier.of("hover_hints", "main"));


    private static boolean toggled;

    private HoverHintKeybinds() {}

    public static void register() {
        SHOW_HINTS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hover_hints.show_hints",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                CATEGORY
        ));
    }

    public static void tick() {
        if (SHOW_HINTS == null) {
            return;
        }

        HoverHintsConfig config = HoverHintsConfigManager.getConfig();
        if(!config.enabled || !config.mode.equals(HintActivationMode.TOGGLE)) return;

        while (SHOW_HINTS.wasPressed()) {
            toggled = !toggled;
        }
    }

    public static boolean isHintModeHeld() {
        return SHOW_HINTS != null && SHOW_HINTS.isPressed();
    }

    public static boolean isHintModeToggled() {
        return toggled;
    }
}
