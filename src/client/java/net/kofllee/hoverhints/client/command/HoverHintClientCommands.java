package net.kofllee.hoverhints.client.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.kofllee.hoverhints.client.config.HoverHintsConfigDependencies;
import net.kofllee.hoverhints.client.config.HoverHintsConfigScreen;
import net.minecraft.client.MinecraftClient;

public final class HoverHintClientCommands {
    private HoverHintClientCommands() {}

    public static void register() {
        if (!HoverHintsConfigDependencies.hasClothConfig()) {
            return;
        }

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hoverhints")
                    .then(ClientCommandManager.literal("config")
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();

                                client.send(() -> client.setScreen(
                                        HoverHintsConfigScreen.create(client.currentScreen)
                                ));

                                return 1;
                            })
                    )
            );
        });
    }
}