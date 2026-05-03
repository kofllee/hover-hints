package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

import java.util.Optional;

public class AnimalFeedHintProvider implements HintProvider {

    @Override
    public String id() {
        return "animal_feed";
    }

    @Override
    public Optional<HintResult> getHint(HintContext hintContext) {
        if(!(hintContext.hitResult() instanceof EntityHitResult entityHitResult)) {
            return Optional.empty();
        }

        if(!(entityHitResult.getEntity() instanceof AnimalEntity animal)) {
            return Optional.empty();
        }

        if(!animal.isBreedingItem(hintContext.heldStack())){
            return Optional.empty();
        }

        if(animal.isBaby()){
            return Optional.of(new HintResult(HintIcons.GROWTH, Text.translatable("hint.hover_hints.animal_feed_baby").styled(style -> style.withColor(0x55FF55))));
        }

        Identifier id = Registries.ENTITY_TYPE.getId(animal.getType());
        boolean isVanilla = id != null && id.getNamespace().equals("minecraft");

        String key = isVanilla
                ? "hint.hover_hints.animal_can_breed_vanilla"
                : "hint.hover_hints.animal_can_breed";

        return Optional.of(new HintResult(
                HintIcons.GROWTH,
                Text.translatable(key)
                        .styled(style -> style.withColor(0x55FF55))
        ));
    }
}
