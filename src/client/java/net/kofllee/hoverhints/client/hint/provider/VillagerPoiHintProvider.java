package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.villager.ClientVillagerPoiState;
import net.kofllee.hoverhints.client.villager.VillagerPoiRequestSender;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

import java.util.List;
import java.util.Optional;

public class VillagerPoiHintProvider implements HintProvider {
    @Override
    public String id() {
        return "villager_poi";
    }

    @Override
    public boolean requiresServer() {
        return true;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!hintContext.heldStack().is(Items.EMERALD)) {
            return;
        }

        if (!(hintContext.hitResult() instanceof BlockHitResult blockHit)) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHit.getBlockPos());

        if (!isVillagerPoi(state) && !(state.getBlock() instanceof BedBlock)) {
            return;
        }

        VillagerPoiRequestSender.request(blockHit.getBlockPos());

        Boolean occupied = ClientVillagerPoiState.get(blockHit.getBlockPos());

        if (occupied == null) {
            return;
        }

        out.add(new HintResult(
                Items.EMERALD.getDefaultInstance(),
                Component.translatable(
                        occupied
                                ? "hint.hover_hints.villager_poi_occupied"
                                : "hint.hover_hints.villager_poi_free"
                ).withStyle(style -> style.withColor(occupied ? 0xFFAA00 : 0x55FF55))
        ));
    }

    private static boolean isVillagerPoi(BlockState state) {
        Optional<Holder<PoiType>> poi =
                PoiTypes.forState(state);

        if (poi.isEmpty()) {
            return false;
        }

        return poi.get().value().maxTickets() > 0;
    }
}