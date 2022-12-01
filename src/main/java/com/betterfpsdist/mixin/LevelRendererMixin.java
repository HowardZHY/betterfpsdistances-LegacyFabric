package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.BuiltChunk;
import net.minecraft.client.world.ChunkAssemblyHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class LevelRendererMixin
{
    @Shadow
    @Final
    private MinecraftClient client;

    private BuiltChunk current = null;

    //private HashSet<BlockPos>                 renderedPositions = new HashSet<>();
    //private long                              nextUpdate        = 0;

    @Redirect(method = "renderLayer*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/BuiltChunk;method_10170()Lnet/minecraft/client/world/ChunkAssemblyHelper;"))
    public ChunkAssemblyHelper on(final BuiltChunk instance)
    {
        current = instance;
        return instance.method_10170();
    }

    @Redirect(method = "renderLayer*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ChunkAssemblyHelper;method_10149(Lnet/minecraft/client/render/RenderLayer;)Z"))
    public boolean on(final ChunkAssemblyHelper instance, final RenderLayer layer)
    {
        boolean returnv =
          client.getCameraEntity() != null
            && distSqr(client.getCameraEntity().getPos(), new Vec3d(current.getPos().getX(), current.getPos().getY(), current.getPos().getZ()))
                 > (client.options.viewDistance * 16) * (client.options.viewDistance * 16)
            || instance.method_10149(layer);

/*
        if (Minecraft.getInstance().player.level.getGameTime() > nextUpdate)
        {
            nextUpdate = Minecraft.getInstance().player.level.getGameTime() + 20 * 5;
            BetterfpsdistMod.LOGGER.warn("Rendered Sections:" + renderedPositions.size());
            renderedPositions.clear();
        }

        if (!returnv)
        {
            renderedPositions.add(current.getOrigin());
        }*/

        return returnv;
    }

    private double distSqr(Vec3d from, Vec3d to)
    {
        double d0 = from.x - to.x;
        double d1 = from.y - to.y;
        double d2 = from.z - to.z;
        return d0 * d0 + BetterfpsdistMod.config.getCommonConfig().stretch * (d1 * d1) + d2 * d2;
    }
}
