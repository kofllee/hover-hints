package net.kofllee.hoverhints.client.hint;

import net.minecraft.util.Identifier;

public final class HintIcons {
    public static final Identifier XP_ORB = id("xp_orb");
    public static Identifier FIRE = id("fire");
    public static Identifier GROWTH = id("growth");

    private static Identifier id(String path) {
        return Identifier.of("hover_hints", "textures/gui/icons/" + path + ".png");
    }
}
