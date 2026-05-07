package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.util.AnimalHintItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.TraderLlama;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

public class TameableAnimalHintProvider implements HintProvider {

    @Override
    public String id() {
        return "tameable_animal";
    }

    @Override
    public boolean requiresServer() {
        return false;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity entity = entityHitResult.getEntity();

        if (entity instanceof Wolf wolf) {
            if (wolf.isTame() || !hintContext.heldStack().is(Items.BONE)) {
                return;
            }

            out.add(tameChance(33.3f));
        }

        if (entity instanceof Cat cat) {
            if (cat.isTame() || !cat.isFood(hintContext.heldStack())) {
                return;
            }

            out.add(tameChance(33.3f));
        }

        if (entity instanceof Parrot parrot) {
            if (parrot.isTame() || !AnimalHintItems.isParrotTamingItem(hintContext.heldStack())) {
                return;
            }

            out.add(tameChance(10f));
        }

        if (entity instanceof Ocelot ocelot) {
            if (!ocelot.isFood(hintContext.heldStack())) {
                return;
            }

            out.add(trustChance(33.3f));
        }

        if (entity instanceof Fox fox) {
            if (!fox.isFood(hintContext.heldStack())) {
                return;
            }

            out.add(trustChance(100f));
        }

        if (entity instanceof AbstractNautilus nautilus) {
            if (nautilus.isTame() || !nautilus.isFood(hintContext.heldStack())) {
                return;
            }

            out.add(tameChance(33.3f));
        }

        if (entity instanceof Horse ||
                entity instanceof Donkey ||
                entity instanceof Mule ||
                entity instanceof Llama ||
                entity instanceof TraderLlama ||
                entity instanceof ZombieHorse) {
            if(((AbstractHorse) entity).isTamed()){
                return;
            }

            out.add(new HintResult(
                    HintIcons.HEARTS,
                    Component.translatable("hint.hover_hints.ride_to_tame")
                            .withStyle(style -> style.withColor(0xFF5555))
            ));
        }
    }

    private HintResult tameChance(float percent) {
        return new HintResult(
                HintIcons.HEARTS,
                Component.translatable("hint.hover_hints.tame_chance", percent)
                        .withStyle(style -> style.withColor(0xFF5555))
        );
    }

    private HintResult trustChance(float percent) {
        return new HintResult(
                HintIcons.HEARTS,
                Component.translatable("hint.hover_hints.trust_chance", percent)
                        .withStyle(style -> style.withColor(0xFF5555))
        );
    }
}
