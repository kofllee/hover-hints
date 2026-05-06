package net.kofllee.hoverhints.client.config;

import net.minecraft.text.Text;

import java.util.Locale;

public enum HintAnchor{
    BELOW_CROSSHAIR(0, 4),
    ABOVE_CROSSHAIR(0, -4),

    SCREEN_TOP_LEFT(2, 2),
    SCREEN_TOP_CENTER(0, 2),
    SCREEN_TOP_RIGHT(-2, 2),

    SCREEN_CENTER_LEFT(2, 0),
    SCREEN_CENTER(0, 0),
    SCREEN_CENTER_RIGHT(-2, 0);

    public final int defaultOffsetX;
    public final int defaultOffsetY;

    HintAnchor(int defaultOffsetX, int defaultOffsetY) {
        this.defaultOffsetX = defaultOffsetX;
        this.defaultOffsetY = defaultOffsetY;
    }

    public static Text getDisplayName(Enum anEnum) {
        return Text.translatable("config.hover_hints.anchor." + anEnum.name().toLowerCase(Locale.ROOT));
    }
}
