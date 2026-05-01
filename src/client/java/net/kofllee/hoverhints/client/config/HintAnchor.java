package net.kofllee.hoverhints.client.config;

public enum HintAnchor{
    BELOW_CROSSHAIR(0, 16),
    ABOVE_CROSSHAIR(0, -16),

    SCREEN_TOP_LEFT(4, 4),
    SCREEN_TOP_CENTER(0, 4),
    SCREEN_TOP_RIGHT(-4, 4),

    SCREEN_CENTER_LEFT(4, 0),
    SCREEN_CENTER(0, 0),
    SCREEN_CENTER_RIGHT(-4, 0);

    public final int defaultOffsetX;
    public final int defaultOffsetY;

    HintAnchor(int defaultOffsetX, int defaultOffsetY) {
        this.defaultOffsetX = defaultOffsetX;
        this.defaultOffsetY = defaultOffsetY;
    }
}
