package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

public final class SulfurCubeHintProvider implements HintProvider {

    @Override
    public String id() {
        return "sulfur_cube";
    }

    @Override
    public boolean requiresServer() {
        return false;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof EntityHitResult entityHit)) {
            return;
        }

        if (!(entityHit.getEntity() instanceof SulfurCube cube)) {
            return;
        }

        ItemStack stack = hintContext.heldStack();

        if (stack.isEmpty()) {
            return;
        }

        if (cube.isBaby()) {
            addGrowthHint(stack, out);
            return;
        }

        addArchetypeHint(hintContext, stack, out);
    }

    private static void addGrowthHint(ItemStack stack, List<HintResult> out) {
        if (!stack.is(ItemTags.SULFUR_CUBE_FOOD)) {
            return;
        }

        out.add(new HintResult(
                stack,
                Component.translatable("hint.hover_hints.sulfur_cube_growth", 10)
                        .withStyle(style -> style.withColor(0x55FF55))
        ));
    }

    private static void addArchetypeHint(HintContext context, ItemStack stack, List<HintResult> out
    ) {
        if (!stack.is(ItemTags.SULFUR_CUBE_SWALLOWABLE)) {
            return;
        }

        HolderLookup.RegistryLookup<SulfurCubeArchetype> registry =
                context.world()
                        .registryAccess()
                        .lookupOrThrow(Registries.SULFUR_CUBE_ARCHETYPE);

        Holder.Reference<SulfurCubeArchetype> archetype =
                registry.filterElements(a -> stack.is(a.items()))
                        .listElements()
                        .findFirst()
                        .orElse(null);

        if (archetype == null) {
            return;
        }

        Identifier id = archetype.key().identifier();

        out.add(new HintResult(
                stack,
                Component.translatable(
                        "hint.hover_hints.sulfur_cube_archetype",
                        Component.translatable(
                                "hint.hover_hints.sulfur_cube_archetype." + id.getPath()
                        )
                ).withStyle(style -> style.withColor(0xFFD966))
        ));
    }
}