package net.kofllee.hoverhints.mixin.loot;

import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LeafEntry.class)
public interface LeafEntryAccessor {

    @Accessor("weight")
    int hoverHints$getWeight();

    @Accessor("functions")
    List<LootFunction> hoverHints$getFunctions();
}
