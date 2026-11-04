package tempestclient;

import net.minecraft.client.Minecraft;

/**
 * A reversible, conservative performance profile. It lowers render and
 * simulation distance only when they are currently above the profile targets,
 * and disables entity shadows. Previous values are restored when switched off.
 */
public final class BalancedPerformance {
    private static boolean enabled;
    private static boolean previousEntityShadows;
    private static int previousRenderDistance;
    private static int previousSimulationDistance;

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
            previousRenderDistance = client.options.renderDistance().get();
            previousSimulationDistance = client.options.simulationDistance().get();

            client.options.entityShadows().set(false);
            client.options.renderDistance().set(Math.min(previousRenderDistance, 8));
            client.options.simulationDistance().set(Math.min(previousSimulationDistance, 5));
            enabled = true;
        }
    }

    public static void restore() {
        if (enabled) restore(Minecraft.getInstance());
    }

    private static void restore(Minecraft client) {
        client.options.entityShadows().set(previousEntityShadows);
        client.options.renderDistance().set(previousRenderDistance);
        client.options.simulationDistance().set(previousSimulationDistance);
        enabled = false;
    }
}
