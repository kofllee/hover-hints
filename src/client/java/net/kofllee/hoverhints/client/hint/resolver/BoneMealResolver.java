package net.kofllee.hoverhints.client.hint.resolver;

import net.minecraft.block.*;

import java.util.Optional;

public final class BoneMealResolver {

    public static Optional<BoneMealInfo> resolve(BlockState state){
        Block block = state.getBlock();

        if(block instanceof TorchflowerBlock || block instanceof PitcherCropBlock) {
            return Optional.of(BoneMealInfo.stages(1, 1));
        }

        if (block instanceof CropBlock || block instanceof StemBlock) {
            if (block instanceof BeetrootsBlock) {
                return Optional.of(BoneMealInfo.chanceStage(75, 1));
            }

            return Optional.of(BoneMealInfo.stages(2, 5));
        }

        if (block instanceof SaplingBlock || block instanceof AzaleaBlock) {
            return Optional.of(BoneMealInfo.chance(45));
        }

        if (block instanceof MushroomPlantBlock || block instanceof FungusBlock) {
            return Optional.of(BoneMealInfo.chance(40));
        }

        if (block instanceof BambooBlock || block instanceof BambooShootBlock) {
            return Optional.of(BoneMealInfo.blocks(1, 2));
        }

        if (block instanceof CocoaBlock || block instanceof SweetBerryBushBlock) {
            return Optional.of(BoneMealInfo.stages(1, 1));
        }

        if (block instanceof FlowerbedBlock) {
            int amount = state.get(FlowerbedBlock.FLOWER_AMOUNT);

            if(amount < 4){
                return Optional.of(BoneMealInfo.stages(1, 1));
            }

            return Optional.of(BoneMealInfo.dropsItem());
        }

        if (block instanceof KelpPlantBlock || block instanceof KelpBlock || block instanceof BigDripleafBlock || block instanceof BigDripleafStemBlock) {
            return Optional.of(BoneMealInfo.blocks(1, 1));
        }

        if (block instanceof WeepingVinesBlock || block instanceof WeepingVinesPlantBlock ||
                block instanceof TwistingVinesBlock || block instanceof TwistingVinesPlantBlock) {
            return Optional.of(BoneMealInfo.unboundedBlocks(1));
        }

        if (block instanceof TallFlowerBlock) {
            return Optional.of(BoneMealInfo.dropsItem());
        }

        if (block instanceof ShortPlantBlock ||
                block instanceof SeagrassBlock) {
            return Optional.of(BoneMealInfo.taller());
        }
        if (block instanceof SmallDripleafBlock) {
            return Optional.of(BoneMealInfo.taller());
        }

        if (block instanceof SeaPickleBlock ||
                block instanceof GlowLichenBlock ||
                block instanceof MossBlock) {
            return Optional.of(BoneMealInfo.spread());
        }

        if (block == Blocks.NETHERRACK) {
            return Optional.of(BoneMealInfo.spread());
        }

        return Optional.of(BoneMealInfo.generic());
    }
}
