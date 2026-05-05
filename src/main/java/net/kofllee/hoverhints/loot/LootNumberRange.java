package net.kofllee.hoverhints.loot;

public record LootNumberRange (int min, int max){
    public static LootNumberRange one(){
        return new LootNumberRange(1, 1);
    }
}
