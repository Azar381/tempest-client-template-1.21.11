package tempestclient;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleStatus;

/**
 * A conservative balanced profile that changes only two client graphics
 * options and restores their previous values when disabled.
 */
public final class BalancedPerformance {
    private static boolean enabled;
    private static ParticleStatus previousParticles;
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
            previousParticles = client.options.particles().get();
            previousEntityShadows = client.options.entityShadows().get();

            client.options.particles().set(ParticleStatus.DECREASED);
            client.options.entityShadows().set(false);
            enabled = true;
        }
    }

    public static void restore() {
        if (enabled) restore(Minecraft.getInstance());
    }

    private static void restore(Minecraft client) {
        client.options.particles().set(previousParticles);
        client.options.entityShadows().set(previousEntityShadows);
        enabled = false;
    }
}
