package tempestclient;

import net.minecraft.client.Minecraft;

/** Controls the client-side brightness profile and restores the user's gamma. */
public final class FullbrightController {
    private static boolean enabled;
    private static float brightness = 1.0f;
    private static double previousGamma = 0.5;

    private FullbrightController() {}

    public static boolean isEnabled() { return enabled; }
    public static float getBrightness() { return brightness; }

    public static void toggle() {
        Minecraft client = Minecraft.getInstance();
        if (enabled) {
            restore();
        } else {
            previousGamma = client.options.gamma().get();
            enabled = true;
            applyGamma(client);
        }
    }

    public static void setBrightness(double value) {
        brightness = (float) Math.max(0.0, Math.min(1.0, value));
        if (enabled) applyGamma(Minecraft.getInstance());
    }

    private static void applyGamma(Minecraft client) {
        client.options.gamma().set(Math.max(previousGamma, (double) brightness));
    }

    public static void restore() {
        if (!enabled) return;
        Minecraft.getInstance().options.gamma().set(previousGamma);
        enabled = false;
    }
}
