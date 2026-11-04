package tempestclient;

import net.minecraft.client.Minecraft;

/**
 * A conservative client-side profile that reduces entity-shadow rendering
 * and restores the previous setting when disabled.
 */
public final class BalancedPerformance {
    private static boolean enabled;
    private static boolean previousEntityShadows;

    private BalancedPerformance() {}

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggle() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        if (enabled) {
            restore(client);
        } else {
            previousEntityShadows = client.options.entityShadows().get();
            client.options.entityShadows().set(false);
            enabled = true;
        }
    }

    public static void restore() {
        if (enabled) restore(Minecraft.getInstance());
    }

    private static void restore(Minecraft client) {
        client.options.entityShadows().set(previousEntityShadows);
        enabled = false;
    }
}
