package net.kofllee.hoverhints.client.hint.render;

import net.kofllee.hoverhints.client.config.HintAnchor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec2f;

public final class HintAnchorResolver {

    public static Vec2f resolveAnchor(HintAnchor anchor, MinecraftClient client, int width, int height, int hintWidth, int hintHeight) {
        Vec2f position = switch (anchor) {
            case SCREEN_TOP_LEFT -> new Vec2f(0, 0);
            case SCREEN_TOP_CENTER -> new Vec2f((width - hintWidth) / 2f, 0);
            case SCREEN_TOP_RIGHT -> new Vec2f(width - hintWidth, 0);
            case SCREEN_CENTER_LEFT -> new Vec2f(0, (height - hintHeight) / 2f);
            case SCREEN_CENTER -> new Vec2f((width - hintWidth) / 2f, (height - hintHeight) / 2f);
            case SCREEN_CENTER_RIGHT -> new Vec2f(width - hintWidth, (height - hintHeight) / 2f);
            case ABOVE_CROSSHAIR -> new Vec2f((width - hintWidth) / 2f, height / 2f - hintHeight);
            case BELOW_CROSSHAIR -> new Vec2f((width - hintWidth) / 2f, height / 2f);
        };

        float scale = (float) client.getWindow().getScaleFactor();

        float offsetX = (int) (anchor.defaultOffsetX * scale);
        float offsetY = (int) (anchor.defaultOffsetY * scale);

        return position.add(new  Vec2f(offsetX, offsetY));
    }
}
