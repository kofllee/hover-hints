package net.kofllee.hoverhints.client.hint;

import net.minecraft.resources.Identifier;

public final class HintIcons {
    public static final Identifier XP_ORB = id("xp_orb");
    public static final Identifier REDSTONE = id("redstone");
    public static Identifier FIRE = id("fire");
    public static Identifier GROWTH = id("growth");
    public static Identifier HEARTS = id("hearts");
    public static Identifier BONE_MEAL = id("bone_meal");

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("hover_hints", "textures/gui/icons/" + path + ".png");
    }
}
