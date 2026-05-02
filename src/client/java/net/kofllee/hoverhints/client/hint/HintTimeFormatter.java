package net.kofllee.hoverhints.client.hint;

import net.minecraft.text.Text;

public final class HintTimeFormatter {
    private HintTimeFormatter() {}

    public static Text formatTicks(int ticks) {
        int totalSeconds = ticks / 20;

        if(totalSeconds < 60) {
            return Text.translatable("hint.hover_hints.time.seconds", totalSeconds);
        }

        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;

        if(seconds == 0){
            return Text.translatable("hint.hover_hints.time.minutes", minutes);
        }

        return Text.translatable("hint.hover_hints.time.minutes_seconds", minutes, seconds);
    }
}
