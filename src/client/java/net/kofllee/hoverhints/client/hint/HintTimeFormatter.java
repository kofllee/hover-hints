package net.kofllee.hoverhints.client.hint;

import net.minecraft.network.chat.Component;

public final class HintTimeFormatter {
    private HintTimeFormatter() {}

    public static Component formatTicks(int ticks) {
        int totalSeconds = ticks / 20;

        if(totalSeconds < 60) {
            return Component.translatable("hint.hover_hints.time.seconds", totalSeconds);
        }

        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;

        if(seconds == 0){
            return Component.translatable("hint.hover_hints.time.minutes", minutes);
        }

        return Component.translatable("hint.hover_hints.time.minutes_seconds", minutes, seconds);
    }
}
