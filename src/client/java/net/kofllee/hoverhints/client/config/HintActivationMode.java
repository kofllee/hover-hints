package net.kofllee.hoverhints.client.config;

import net.minecraft.network.chat.Component;

public enum HintActivationMode {
    HOLD_KEY("hold_key"),
    ALWAYS("always"),
    TOGGLE("toggle_key");

    private final String key;

    HintActivationMode(String key) {
        this.key = key;
    }

    public Component asText() {
        return Component.translatable("config.hover_hints.activation_mode." + this.key);
    }
}
