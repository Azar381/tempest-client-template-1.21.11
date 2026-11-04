package tempestclient.mixin;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import tempestclient.FullbrightController;

@Mixin(LightTexture.class)
public class FullbrightMixin {
    @Inject(
            method = "getBrightness(Lnet/minecraft/world/level/dimension/DimensionType;I)F",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void tempest$boostBrightness(
            DimensionType dimensionType,
            int lightLevel,
            CallbackInfoReturnable<Float> cir
    ) {
        if (FullbrightController.isEnabled()) {
            cir.setReturnValue(Math.max(cir.getReturnValue(), FullbrightController.getBrightness()));
        }
    }
}
