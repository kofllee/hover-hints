package net.kofllee.hoverhints.loot;

public record LootFloatRange(float min, float max) {

    public static LootFloatRange one() {
        return new LootFloatRange(1.0F, 1.0F);
    }
}