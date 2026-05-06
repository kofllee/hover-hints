package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.HoverHintsClient;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.loot.ClientMobLootCache;
import net.kofllee.hoverhints.client.loot.MobLootRequestSender;
import net.kofllee.hoverhints.loot.MobLootEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;

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
        RegistryEntry<Enchantment> lootingEntry =
                hintContext.player()
                        .getRegistryManager()
                        .get(RegistryKeys.ENCHANTMENT)
                        .entryOf(Enchantments.LOOTING);

        int lootingLevel = EnchantmentHelper.getLevel(
                lootingEntry,
                weaponStack
        );

        HoverHintsClient.LOGGER.info("Requesting mob loot info for entity id {} with weapon {} and looting level {}",
                entityId,
                weaponStack.getItem().getName().getString(),
                lootingLevel
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
                    Text.translatable(
                            "hint.hover_hints.mob_loot_entry_short",
                            min,
                            max,
                            entry.chancePercent()
                    ).styled(style -> style.withColor(0xFFD966))
            ));
        }
    }

    private boolean isAttackItem(ItemStack stack) {
        Item item = stack.getItem();

        return item instanceof SwordItem
                || item instanceof AxeItem
                || item instanceof TridentItem
                || item instanceof MaceItem;
    }
}
