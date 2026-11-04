package tempestclient;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

import tempestclient.gui.KeystrokesHud;
import tempestclient.gui.TempestScreen;

public class TempestClientClient implements ClientModInitializer {

    private static KeyMapping openGuiKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category =
                KeyMapping.Category.register(
                        Identifier.fromNamespaceAndPath("tempest-client", "general")
                );

        openGuiKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.tempest-client.open_gui",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_F10,
                        category
                )
        );

        // Client-side HUD overlay: reads local input only.
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("tempest-client", "keystrokes"),
                KeystrokesHud::render
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.consumeClick()) {
                if (client.screen instanceof TempestScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new TempestScreen());
                }
            }
        });
    }
}
