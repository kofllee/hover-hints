package net.kofllee.hoverhints.client.hint.util;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.AzaleaBlock;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.BigDripleafBlock;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableFeaturePlacerBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.FireflyBushBlock;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.NetherFungusBlock;
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.HangingMossBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.KelpPlantBlock;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.ShortDryGrassBlock;
import net.minecraft.world.level.block.SmallDripleafBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.TallDryGrassBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.TwistingVinesBlock;
import net.minecraft.world.level.block.TwistingVinesPlantBlock;
import net.minecraft.world.level.block.WeepingVinesBlock;
import net.minecraft.world.level.block.WeepingVinesPlantBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class BoneMealResolver {

    public static Optional<BoneMealInfo> resolve(BlockState state){
        Block block = state.getBlock();

        if(block instanceof TorchflowerCropBlock || block instanceof PitcherCropBlock) {
            return Optional.of(BoneMealInfo.stages(1, 1));
        }

        if (block instanceof CropBlock || block instanceof StemBlock) {
            if (block instanceof BeetrootBlock) {
                return Optional.of(BoneMealInfo.chanceStage(75, 1));
            }

            return Optional.of(BoneMealInfo.stages(2, 5));
        }

        if (block instanceof SaplingBlock || block instanceof AzaleaBlock) {
            return Optional.of(BoneMealInfo.chance(45));
        }

        if (block instanceof MushroomBlock || block instanceof NetherFungusBlock) {
            return Optional.of(BoneMealInfo.chance(40));
        }

        if (block instanceof BambooStalkBlock || block instanceof BambooSaplingBlock) {
            return Optional.of(BoneMealInfo.blocks(1, 2));
        }

        if (block instanceof CocoaBlock || block instanceof SweetBerryBushBlock) {
            return Optional.of(BoneMealInfo.stages(1, 1));
        }

        if (block instanceof FlowerBedBlock) {
            int amount = state.getValue(FlowerBedBlock.AMOUNT);

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

        if (block instanceof TallGrassBlock ||
                block instanceof SeagrassBlock) {
            return Optional.of(BoneMealInfo.taller());
        }
        if (block instanceof SmallDripleafBlock) {
            return Optional.of(BoneMealInfo.taller());
        }

        if (block instanceof SeaPickleBlock ||
                block instanceof GlowLichenBlock ||
                block instanceof BonemealableFeaturePlacerBlock) {
            return Optional.of(BoneMealInfo.spread());
        }

        if (block == Blocks.NETHERRACK) {
            return Optional.of(BoneMealInfo.spread());
        }

        if (block instanceof HangingMossBlock) {
            return Optional.of(BoneMealInfo.blocks(1, 1));
        }

        if (block instanceof BushBlock || block instanceof FireflyBushBlock) {
            return Optional.of(BoneMealInfo.spread());
        }

        if (block instanceof ShortDryGrassBlock) {
            return Optional.of(BoneMealInfo.taller());
        }

        if (block instanceof TallDryGrassBlock) {
            return Optional.of(BoneMealInfo.spread());
        }

        if (block instanceof LeafLitterBlock) {
            return Optional.of(BoneMealInfo.stages(1, 1));
        }

        return Optional.of(BoneMealInfo.generic());
    }
}
