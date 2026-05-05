package net.kofllee.hoverhints.mixin.loot;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface LootPoolAccessor {

    @Accessor("entries")
    List<LootPoolEntry> hoverHints$getEntries();

    @Accessor("conditions")
    List<LootCondition> hoverHints$getConditions();

    @Accessor("functions")
    List<LootFunction> hoverHints$getFunctions();
}