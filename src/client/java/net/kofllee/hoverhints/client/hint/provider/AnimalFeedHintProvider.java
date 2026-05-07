package net.kofllee.hoverhints.client.hint.provider;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.client.animal.AnimalAgeRequestSender;
import net.kofllee.hoverhints.client.animal.ClientAnimalAgeState;
import net.kofllee.hoverhints.client.hint.*;
import net.kofllee.hoverhints.network.AnimalAgeRequestPayload;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

public class AnimalFeedHintProvider implements HintProvider {

    @Override
    public String id() {
        return "animal_feed";
    }

    @Override
    public boolean requiresServer() {
        return true;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if(!(hintContext.hitResult() instanceof EntityHitResult entityHitResult)) {
            return;
        }

        if(!(entityHitResult.getEntity() instanceof Animal animal)) {
            return;
        }

        if(!animal.isFood(hintContext.heldStack())){
            return;
        }

        boolean hasServer = ClientPlayNetworking.canSend(AnimalAgeRequestPayload.ID);
        if (hasServer) {
            AnimalAgeRequestSender.request(animal.getId());

            var snapshotOpt = ClientAnimalAgeState.getLive(animal.getId());

            if (snapshotOpt.isPresent()) {
                var snapshot = snapshotOpt.get();

                int age = snapshot.breedingAge();
                int loveTicks = snapshot.loveTicks();

                if (age < 0) {
                    out.add(new HintResult(
                            HintIcons.HEARTS,
                            Component.translatable(
                                    "hint.hover_hints.animal_grows_in",
                                    HintTimeFormatter.formatTicks(-age)
                            ).withStyle(style -> style.withColor(0x55FF55))
                    ));
                    return;
                }

                if (age > 0) {
                    out.add(new HintResult(
                            HintIcons.HEARTS,
                            Component.translatable(
                                    "hint.hover_hints.animal_breed_cooldown",
                                    HintTimeFormatter.formatTicks(age)
                            ).withStyle(style -> style.withColor(0xFFD966))
                    ));
                    return;
                }

                if (loveTicks > 0) {
                    out.add(new HintResult(
                            HintIcons.HEARTS,
                            Component.translatable(
                                    "hint.hover_hints.animal_love_time",
                                    HintTimeFormatter.formatTicks(loveTicks)
                            ).withStyle(style -> style.withColor(0xFF5555))
                    ));
                    return;
                }

                out.add(new HintResult(
                        HintIcons.HEARTS,
                        Component.translatable("hint.hover_hints.animal_can_breed")
                                .withStyle(style -> style.withColor(0xFF5555))
                ));
                return;
            }
        }

        if(animal.isBaby()){
            out.add(new HintResult(HintIcons.HEARTS, Component.translatable("hint.hover_hints.animal_feed_baby").withStyle(style -> style.withColor(0x55FF55))));
            return;
        }

        Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType());
        boolean isVanilla = id.getNamespace().equals("minecraft");

        if(isVanilla){
            out.add(new HintResult(
                    HintIcons.HEARTS,
                    Component.translatable("hint.hover_hints.animal_can_breed")
                            .withStyle(style -> style.withColor(0xFF5555)).
                            append(Component.translatable("hint.hover_hints.animal_can_breed.vanilla_suffix")
                                    .withStyle(style -> style.withColor(0xAAAAAA)))
            ));
            return;
        }

        out.add(new HintResult(
                HintIcons.HEARTS,
                Component.translatable("hint.hover_hints.animal_can_breed")
                        .withStyle(style -> style.withColor(0xFF5555))
        ));
    }
}
