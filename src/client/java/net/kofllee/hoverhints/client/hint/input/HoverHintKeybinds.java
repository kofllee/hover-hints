package net.kofllee.hoverhints.client.hint.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kofllee.hoverhints.client.config.HintActivationMode;
import net.kofllee.hoverhints.client.config.HoverHintsConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class HoverHintKeybinds {
    public static KeyMapping SHOW_HINTS;

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("hover_hints", "main"));


    private static boolean toggled;

    private HoverHintKeybinds() {}

    public static void register() {
        SHOW_HINTS = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.hover_hints.show_hints",
                InputConstants.Type.KEYSYM,
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

        while (SHOW_HINTS.consumeClick()) {
            toggled = !toggled;
        }
    }

    public static boolean isHintModeHeld() {
        return SHOW_HINTS != null && SHOW_HINTS.isDown();
    }

    public static boolean isHintModeToggled() {
        return toggled;
    }
}
