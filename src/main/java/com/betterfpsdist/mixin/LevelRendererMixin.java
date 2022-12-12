package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class LevelRendererMixin
{
    @Shadow
    @Final
    private Minecraft mc;

    private RenderChunk current = null;

    //private HashSet<BlockPos>                 renderedPositions = new HashSet<>();
    //private long                              nextUpdate        = 0;

    @Redirect(method = "renderBlockLayer*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;getCompiledChunk()Lnet/minecraft/client/renderer/chunk/CompiledChunk;"))
    public CompiledChunk on(final RenderChunk instance)
    {
        current = instance;
        return instance.getCompiledChunk();
    }

    @Redirect(method = "renderBlockLayer*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/CompiledChunk;isLayerEmpty(Lnet/minecraft/util/EnumWorldBlockLayer;)Z"))
    public boolean on(final CompiledChunk instance, final EnumWorldBlockLayer layer)
    {
        boolean returnv =
          mc.getRenderViewEntity() != null
            && distSqr(mc.getRenderViewEntity().getPositionVector(), new Vec3(current.getPosition().getX(), current.getPosition().getY(), current.getPosition().getZ()))
                 > (mc.gameSettings.renderDistanceChunks * 16) * (mc.gameSettings.renderDistanceChunks * 16)
            || instance.isLayerEmpty(layer);

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

    private double distSqr(Vec3 from, Vec3 to)
    {
        double d0 = from.xCoord - to.xCoord;
        double d1 = from.yCoord - to.yCoord;
        double d2 = from.zCoord - to.zCoord;
        return d0 * d0 + BetterfpsdistMod.config.getCommonConfig().stretch * (d1 * d1) + d2 * d2;
    }
}
