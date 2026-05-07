package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.loot.ClientMobLootCache;
import net.kofllee.hoverhints.client.loot.MobLootRequestSender;
import net.kofllee.hoverhints.loot.MobLootEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

public class MobLootHintProvider implements HintProvider {
    @Override
    public String id() {
        return "mob_loot";
    }

    @Override
    public boolean requiresServer() {
        return true;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof EntityHitResult entityHit)) {
            return;
        }

        if (!(entityHit.getEntity() instanceof LivingEntity living)) {
            return;
        }

        ItemStack weaponStack = hintContext.heldStack();

        if (!isAttackItem(weaponStack)) {
            return;
        }

        int entityId = living.getId();
        Holder<Enchantment> lootingEntry =
                hintContext.player()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.LOOTING);

        int lootingLevel = EnchantmentHelper.getItemEnchantmentLevel(
                lootingEntry,
                weaponStack
        );

        MobLootRequestSender.request(entityId, weaponStack, lootingLevel);
        List<MobLootEntry> entries = ClientMobLootCache.get(entityId);

        if (entries.isEmpty()) {
            return;
        }

        for (MobLootEntry entry : entries) {
            int min = Math.max(0, entry.minCount());
            int max = Math.max(0, entry.maxCount());

            out.add(new HintResult(
                    new ItemStack(entry.item()),
                    Component.translatable(
                            "hint.hover_hints.mob_loot_entry_short",
                            min,
                            max,
                            entry.chancePercent()
                    ).withStyle(style -> style.withColor(0xFFD966))
            ));
        }
    }


    private boolean isAttackItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ItemAttributeModifiers modifiers =
                stack.getOrDefault(
                        DataComponents.ATTRIBUTE_MODIFIERS,
                        ItemAttributeModifiers.EMPTY
                );

        return modifiers.modifiers().stream().anyMatch(entry ->
                entry.attribute().equals(Attributes.ATTACK_DAMAGE)
                        && entry.modifier().amount() > 0
        );
    }
}
