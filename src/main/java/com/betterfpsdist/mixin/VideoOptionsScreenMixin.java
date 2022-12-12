package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(GuiVideoSettings.class)
public class VideoOptionsScreenMixin
{
    @Shadow
    @Final
    @Mutable
    private static GameSettings.Options[] videoOptions;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void on(final CallbackInfo ci)
    {
        try
        {
            final List<Object> options = new ArrayList<>(Arrays.asList(videoOptions));
            options.add(options.indexOf(GameSettings.Options.GUI_SCALE) + 1);
            videoOptions = options.toArray(new GameSettings.Options[0]);
        }
        catch (Throwable e)
        {
            BetterfpsdistMod.LOGGER.error("Error trying to add an option Button to video settings, likely optifine is present which removes vanilla functionality required."
                                            + " The mod still works, but you'll need to manually adjust the config to get different Render distance stretch values as the button could not be added.");
        }
    }
}
