package net.kofllee.hoverhints.client.animal;

import net.minecraft.client.MinecraftClient;

import java.util.Optional;

public final class ClientAnimalAgeState {
    private static int entityId = -1;
    private static int breedingAge = 0;
    private static int loveTicks = 0;
    private static long receivedTick = -1;

    private ClientAnimalAgeState() {}

    public static void set(int id, int age, int love) {
        MinecraftClient client = MinecraftClient.getInstance();

        entityId = id;
        breedingAge = age;
        loveTicks = love;
        receivedTick = client.world == null ? -1 : client.world.getTime();
    }

    public static Optional<AnimalAgeSnapshot> getLive(int id) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world == null || entityId != id || receivedTick < 0) {
            return Optional.empty();
        }

        long passed = client.world.getTime() - receivedTick;

        int liveBreedingAge = breedingAge;

        if (breedingAge < 0) {
            liveBreedingAge = (int) Math.min(0, breedingAge + passed);
        } else if (breedingAge > 0) {
            liveBreedingAge = (int) Math.max(0, breedingAge - passed);
        }

        int liveLoveTicks = (int) Math.max(0, loveTicks - passed);

        return Optional.of(new AnimalAgeSnapshot(liveBreedingAge, liveLoveTicks));
    }

    public record AnimalAgeSnapshot(int breedingAge, int loveTicks) {}
}