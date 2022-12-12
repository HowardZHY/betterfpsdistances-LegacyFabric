package com.betterfpsdist;

import com.betterfpsdist.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file

@Mod(modid = "betterfpsdist", clientSideOnly = true)

public class BetterfpsdistMod implements IFMLLoadingPlugin {

    public static final String MODID  = "betterfpsdist";

    public static final Logger LOGGER = LogManager.getLogger();

    public static Configuration config;

    public BetterfpsdistMod() {
        MixinBootstrap.init();
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.UNKNOWN);
        Mixins.addConfiguration("betterfpsdist.mixins.json");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        config = new Configuration();
        config.load();
        LOGGER.info(MODID + " mod initialized");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
