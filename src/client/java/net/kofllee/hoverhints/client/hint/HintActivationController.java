package net.kofllee.hoverhints.client.hint;

import net.kofllee.hoverhints.client.config.HoverHintsConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.input.HoverHintKeybinds;

public final class HintActivationController {
    private HintActivationController() {}

    public static boolean shouldShowHints() {
        HoverHintsConfig config = HoverHintsConfigManager.getConfig();

        if (!config.enabled) {
            return false;
        }

        return switch (config.mode) {
            case ALWAYS -> true;
            case HOLD_KEY -> HoverHintKeybinds.isHintModeHeld();
            case TOGGLE -> HoverHintKeybinds.isHintModeToggled();
        };
    }
}
