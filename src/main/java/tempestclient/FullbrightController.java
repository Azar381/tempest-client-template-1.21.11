package tempestclient;

public final class FullbrightController {
    private static boolean enabled;
    private static float brightness = 1.0f;

    private FullbrightController() {}

    public static boolean isEnabled() {
        return enabled;
    }

    public static float getBrightness() {
        return brightness;
    }

    public static void toggle() {
        enabled = !enabled;
    }

    public static void setBrightness(double value) {
        brightness = (float) Math.max(0.0, Math.min(1.0, value));
    }

    public static void restore() {
        enabled = false;
    }
}
