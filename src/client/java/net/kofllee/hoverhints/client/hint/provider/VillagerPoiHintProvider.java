package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.villager.ClientVillagerPoiState;
import net.kofllee.hoverhints.client.villager.VillagerPoiRequestSender;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.List;
import java.util.Optional;

public class VillagerPoiHintProvider implements HintProvider {
    @Override
    public String id() {
        return "villager_poi";
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!hintContext.heldStack().isOf(Items.EMERALD)) {
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
                Items.EMERALD.getDefaultStack(),
                Text.translatable(
                        occupied
                                ? "hint.hover_hints.villager_poi_occupied"
                                : "hint.hover_hints.villager_poi_free"
                ).styled(style -> style.withColor(occupied ? 0xFFAA00 : 0x55FF55))
        ));
    }

    private static boolean isVillagerPoi(BlockState state) {
        Optional<RegistryEntry<PointOfInterestType>> poi =
                PointOfInterestTypes.getTypeForState(state);

        if (poi.isEmpty()) {
            return false;
        }

        return poi.get().value().ticketCount() > 0;
    }
}