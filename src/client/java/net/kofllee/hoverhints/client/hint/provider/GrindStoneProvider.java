package net.kofllee.hoverhints.client.hint.provider;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;

import java.util.List;

public class GrindStoneProvider implements HintProvider {
    @Override
    public String id() {
        return "grindstone";
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

        if(stack.isEmpty() || !stack.hasEnchantments()) {
            return;
        }

        XpRange range = GrindstoneXpCalculator.calculate(stack);

        if(range.isEmpty()) {
            return;
        }

        out.add(new HintResult(HintIcons.XP_ORB, Text.translatable("hint.hover_hints.grindstone_xp", range.min, range.max).styled(style -> style.withColor(0x4ea14c))));

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

            ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(stack);

            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : enchantments.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> enchantment = entry.getKey();
                int level = entry.getIntValue();

                if (!enchantment.isIn(EnchantmentTags.CURSE)) {
                    total += enchantment.value().getMinPower(level);
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
