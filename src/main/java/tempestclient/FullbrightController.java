package tempestclient;

import net.minecraft.client.Minecraft;

/**
 * Client-side brightness control using Minecraft's own gamma option.
 * Vanilla gamma is capped at 1.0; this does not patch the lightmap.
 */
public final class FullbrightController {
    private static boolean enabled;
    private static double previousGamma = 0.5;
    private static double brightness = 1.0;

    private FullbrightController() {}

    public static boolean isEnabled() {
        return enabled;
    }

    public static double getBrightness() {
        return brightness;
    }

    public static void toggle() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        if (enabled) {
            disable(client);
        } else {
            previousGamma = client.options.gamma().get();
            enabled = true;
            apply(client);
        }
    }

    public static void setBrightness(double value) {
        brightness = Math.max(0.0, Math.min(1.0, value));
        if (enabled) apply(Minecraft.getInstance());
    }

    private static void apply(Minecraft client) {
        client.options.gamma().set(brightness);
    }

    private static void disable(Minecraft client) {
        client.options.gamma().set(previousGamma);
        enabled = false;
    }

    public static void restore() {
        Minecraft client = Minecraft.getInstance();
        if (enabled) disable(client);
    }
}
