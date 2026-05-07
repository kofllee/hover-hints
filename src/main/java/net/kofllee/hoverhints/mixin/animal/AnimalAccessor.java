package net.kofllee.hoverhints.mixin.animal;

import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Animal.class)
public interface AnimalAccessor {
    @Accessor("inLove")
    int hoverHints$getInLove();
}