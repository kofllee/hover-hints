package net.kofllee.hoverhints.client.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.kofllee.hoverhints.client.config.HoverHintsConfigDependencies;
import net.kofllee.hoverhints.client.config.HoverHintsConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class HoverHintClientCommands {
    private HoverHintClientCommands() {}

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hoverhints")
                    .then(ClientCommandManager.literal("config")
                            .executes(context -> openConfigScreen())
                    )
            );
        });
    }

    private static int openConfigScreen() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!HoverHintsConfigDependencies.hasClothConfig()) {
            if (client.player != null) {
                client.player.sendMessage(
                        Text.of("Cloth Config is required to open Hover Hints settings."),
                        false
                );
            }

            return 0;
        }

        client.send(() -> client.setScreen(
                HoverHintsConfigScreen.create(null)
        ));

        return 1;
    }
}