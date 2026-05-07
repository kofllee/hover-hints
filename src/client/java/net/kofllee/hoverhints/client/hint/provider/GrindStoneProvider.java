package net.kofllee.hoverhints.client.hint.provider;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class GrindStoneProvider implements HintProvider {
    @Override
    public String id() {
        return "grindstone";
    }

    @Override
    public boolean requiresServer() {
        return false;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if(!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());

        if(!(state.getBlock() instanceof GrindstoneBlock)) {
            return;
        }

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty() || !stack.isEnchanted()) {
            return;
        }

        XpRange range = GrindstoneXpCalculator.calculate(stack);

        if(range.isEmpty()) {
            return;
        }

        out.add(new HintResult(HintIcons.XP_ORB, Component.translatable("hint.hover_hints.grindstone_xp", range.min, range.max).withStyle(style -> style.withColor(0x4ea14c))));

    }

    private static final class GrindstoneXpCalculator{
        public static XpRange calculate(ItemStack stack) {
            int raw = getRawExperience(stack);

            if (raw <= 0) {
                return XpRange.empty();
            }

            int base = (int) Math.ceil(raw / 2.0);
            return new XpRange(base, base + base - 1);
        }

        private static int getRawExperience(ItemStack stack) {
            int total = 0;

            ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

            for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                Holder<Enchantment> enchantment = entry.getKey();
                int level = entry.getIntValue();

                if (!enchantment.is(EnchantmentTags.CURSE)) {
                    total += enchantment.value().getMinCost(level);
                }
            }

            return total;
        }
    }

    private record XpRange(int min, int max) {
        public static XpRange empty() {
            return new XpRange(0, 0);
        }

        public boolean isEmpty() {
            return min <= 0 && max <= 0;
        }
    }
}
