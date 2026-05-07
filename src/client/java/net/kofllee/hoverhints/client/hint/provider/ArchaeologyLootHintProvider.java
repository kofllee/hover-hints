package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.archaeology.ArchaeologyLootEntry;
import net.kofllee.hoverhints.client.archaeology.ArchaeologyLootRequestSender;
import net.kofllee.hoverhints.client.archaeology.ClientArchaeologyLootCache;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

import java.util.List;

public final class ArchaeologyLootHintProvider implements HintProvider {

    @Override
    public String id() {
        return "archaeology_loot";
    }

    @Override
    public boolean requiresServer() {
        return true;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof BlockHitResult blockHit)) {
            return;
        }

        if (!hintContext.heldStack().is(Items.BRUSH)) {
            return;
        }

        BlockPos pos = blockHit.getBlockPos();

        var state = hintContext.world().getBlockState(pos);

        if (!state.is(Blocks.SUSPICIOUS_SAND) && !state.is(Blocks.SUSPICIOUS_GRAVEL)) {
            return;
        }

        ArchaeologyLootRequestSender.request(pos);

        List<ArchaeologyLootEntry> entries = ClientArchaeologyLootCache.get(pos);

        if (entries.isEmpty()) {
            return;
        }


        for (ArchaeologyLootEntry entry : entries) {
            out.add(new HintResult(
                    new ItemStack(entry.item()),
                    Component.translatable(
                            "hint.hover_hints.archaeology_loot_entry",
                            entry.chancePercent()
                    ).withStyle(style -> style.withColor(0xD6B36A))
            ));
        }
    }
}