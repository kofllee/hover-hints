package net.kofllee.hoverhints.client.hint.util;

public record BoneMealInfo(Type type, int min, int max, int chance) {
    public enum Type {
        GENERIC,
        STAGES,
        BLOCKS,
        UNBOUNDED_BLOCKS,
        CHANCE,
        CHANCE_STAGE,
        DROPS_ITEM,
        TALLER,
        SPREAD
    }

    public static BoneMealInfo generic(){
        return new BoneMealInfo(Type.GENERIC, 0, 0, 0);
    }

    public static BoneMealInfo stages(int min, int max){
        return new BoneMealInfo(Type.STAGES, min, max, 0);
    }

    public static BoneMealInfo blocks(int min, int max){
        return new BoneMealInfo(Type.BLOCKS, min, max, 0);
    }

    public static BoneMealInfo unboundedBlocks(int min){
        return new BoneMealInfo(Type.UNBOUNDED_BLOCKS, min, 0, 0);
    }

    public static BoneMealInfo chance(int  chance){
        return new BoneMealInfo(Type.CHANCE, 0, 0, chance);
    }

    public static BoneMealInfo chanceStage(int chance, int stage){
        return new BoneMealInfo(Type.CHANCE_STAGE, stage, stage, chance);
    }

    public static BoneMealInfo dropsItem(){
        return new BoneMealInfo(Type.DROPS_ITEM, 0, 0, 0);
    }

    public static BoneMealInfo taller(){
        return new BoneMealInfo(Type.TALLER, 0, 0, 0);
    }

    public static BoneMealInfo spread(){
        return new BoneMealInfo(Type.SPREAD, 0, 0, 0);
    }
}
