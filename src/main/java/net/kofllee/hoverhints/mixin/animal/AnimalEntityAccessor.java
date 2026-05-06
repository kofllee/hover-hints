package net.kofllee.hoverhints.mixin.animal;

import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnimalEntity.class)
public interface AnimalEntityAccessor {
    @Accessor("loveTicks")
    int hoverHints$getLoveTicks();
}