package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

import java.util.List;

public class AnimalFeedHintProvider implements HintProvider {

    @Override
    public String id() {
        return "animal_feed";
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if(!(hintContext.hitResult() instanceof EntityHitResult entityHitResult)) {
            return;
        }

        if(!(entityHitResult.getEntity() instanceof AnimalEntity animal)) {
            return;
        }

        if(!animal.isBreedingItem(hintContext.heldStack())){
            return;
        }

        if(animal.isBaby()){
            out.add(new HintResult(HintIcons.HEARTS, Text.translatable("hint.hover_hints.animal_feed_baby").styled(style -> style.withColor(0x55FF55))));
            return;
        }

        Identifier id = Registries.ENTITY_TYPE.getId(animal.getType());
        boolean isVanilla = id.getNamespace().equals("minecraft");

        if(isVanilla){
            out.add(new HintResult(
                    HintIcons.HEARTS,
                    Text.translatable("hint.hover_hints.animal_can_breed")
                            .styled(style -> style.withColor(0xFF5555)).
                            append(Text.translatable("hint.hover_hints.animal_can_breed.vanilla_suffix")
                                    .styled(style -> style.withColor(0xAAAAAA)))
            ));
            return;
        }

        out.add(new HintResult(
                HintIcons.HEARTS,
                Text.translatable("hint.hover_hints.animal_can_breed")
                        .styled(style -> style.withColor(0xFF5555))
        ));
    }
}
