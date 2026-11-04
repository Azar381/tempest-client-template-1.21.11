package tempestclient;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

import tempestclient.gui.KeystrokesHud;
import tempestclient.gui.TempestScreen;

public class TempestClientClient implements ClientModInitializer {

    private static KeyMapping openGuiKey;
    private static KeyMapping fullbrightKey;
    private static KeyMapping brightnessDownKey;
    private static KeyMapping brightnessUpKey;
    private static KeyMapping balancedPerformanceKey;

    public static KeyMapping getFullbrightKey() { return fullbrightKey; }
    public static KeyMapping getBrightnessDownKey() { return brightnessDownKey; }
    public static KeyMapping getBrightnessUpKey() { return brightnessUpKey; }
    public static KeyMapping getBalancedPerformanceKey() { return balancedPerformanceKey; }

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category =
                KeyMapping.Category.register(
                        Identifier.fromNamespaceAndPath("tempest-client", "general")
                );

        openGuiKey = register("key.tempest-client.open_gui", GLFW.GLFW_KEY_F10, category);
        fullbrightKey = register("key.tempest-client.fullbright", GLFW.GLFW_KEY_F7, category);
        brightnessDownKey = register("key.tempest-client.brightness_down", GLFW.GLFW_KEY_F8, category);
        brightnessUpKey = register("key.tempest-client.brightness_up", GLFW.GLFW_KEY_F9, category);
        balancedPerformanceKey = register("key.tempest-client.balanced_performance", GLFW.GLFW_KEY_F6, category);

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

            while (fullbrightKey.consumeClick()) {
                FullbrightController.toggle();
            }

            while (brightnessDownKey.consumeClick()) {
                FullbrightController.setBrightness(
                        FullbrightController.getBrightness() - 0.1
                );
            }

            while (brightnessUpKey.consumeClick()) {
                FullbrightController.setBrightness(
                        FullbrightController.getBrightness() + 0.1
                );
            }

            while (balancedPerformanceKey.consumeClick()) {
                BalancedPerformance.toggle();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            FullbrightController.restore();
            BalancedPerformance.restore();
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            FullbrightController.restore();
            BalancedPerformance.restore();
        });
    }

    private static KeyMapping register(String translationKey, int keyCode,
                                       KeyMapping.Category category) {
        return KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        translationKey,
                        InputConstants.Type.KEYSYM,
                        keyCode,
                        category
                )
        );
    }
}
