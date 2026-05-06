package net.kofllee.hoverhints.client.config;

import net.minecraft.text.Text;

public enum HintActivationMode {
    HOLD_KEY("hold_key"),
    ALWAYS("always"),
    TOGGLE("toggle_key");

    private final String key;

    HintActivationMode(String key) {
        this.key = key;
    }

    public Text asText() {
        return Text.translatable("config.hover_hints.activation_mode." + this.key);
    }
}
