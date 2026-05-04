package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.util.AnimalHintItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;

import java.util.List;

public class TameableAnimalHintProvider implements HintProvider {

    @Override
    public String id() {
        return "tameable_animal";
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity entity = entityHitResult.getEntity();

        if (entity instanceof WolfEntity wolf) {
            if (wolf.isTamed() || !hintContext.heldStack().isOf(Items.BONE)) {
                return;
            }

            out.add(tameChance(33.3f));
        }

        if (entity instanceof CatEntity cat) {
            if (cat.isTamed() || !cat.isBreedingItem(hintContext.heldStack())) {
                return;
            }

            out.add(tameChance(33.3f));
        }

        if (entity instanceof ParrotEntity parrot) {
            if (parrot.isTamed() || !AnimalHintItems.isParrotTamingItem(hintContext.heldStack())) {
                return;
            }

            out.add(tameChance(10f));
        }

        if (entity instanceof OcelotEntity ocelot) {
            if (!ocelot.isBreedingItem(hintContext.heldStack())) {
                return;
            }

            out.add(trustChance(33.3f));
        }

        if (entity instanceof FoxEntity fox) {
            if (!fox.isBreedingItem(hintContext.heldStack())) {
                return;
            }

            out.add(trustChance(100f));
        }

        if (entity instanceof HorseEntity ||
                entity instanceof DonkeyEntity ||
                entity instanceof MuleEntity ||
                entity instanceof LlamaEntity ||
                entity instanceof TraderLlamaEntity) {
            out.add(new HintResult(
                    HintIcons.HEARTS,
                    Text.translatable("hint.hover_hints.ride_to_tame")
                            .styled(style -> style.withColor(0xFF5555))
            ));
        }
    }

    private HintResult tameChance(float percent) {
        return new HintResult(
                HintIcons.HEARTS,
                Text.translatable("hint.hover_hints.tame_chance", percent)
                        .styled(style -> style.withColor(0xFF5555))
        );
    }

    private HintResult trustChance(float percent) {
        return new HintResult(
                HintIcons.HEARTS,
                Text.translatable("hint.hover_hints.trust_chance", percent)
                        .styled(style -> style.withColor(0xFF5555))
        );
    }
}
