package net.kofllee.hoverhints.loot;

public record LootChance (float value, boolean known) {
    public static LootChance known(float value){
        return new LootChance(Math.clamp(value, 0.0f, 1.0f), true);
    }

    public static LootChance unknown(){
        return new LootChance(0, false);
    }
}
