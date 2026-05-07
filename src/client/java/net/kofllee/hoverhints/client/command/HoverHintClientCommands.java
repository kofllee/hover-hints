package net.kofllee.hoverhints.client.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.kofllee.hoverhints.client.config.HoverHintsConfigDependencies;
import net.kofllee.hoverhints.client.config.HoverHintsConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class HoverHintClientCommands {
    private HoverHintClientCommands() {}

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommands.literal("hoverhints")
                    .then(ClientCommands.literal("config")
                            .executes(_ -> openConfigScreen())
                    )
            );
        });
    }

    private static int openConfigScreen() {
        Minecraft client = Minecraft.getInstance();

        if (!HoverHintsConfigDependencies.hasClothConfig()) {
            if (client.player != null) {
                client.gui.getChat().addClientSystemMessage(
                        Component.literal("Cloth Config is required to open Hover Hints settings.")
                );
            }

            return 0;
        }

        client.schedule(() -> client.setScreen(
                HoverHintsConfigScreen.create(null)
        ));

        return 1;
    }
}