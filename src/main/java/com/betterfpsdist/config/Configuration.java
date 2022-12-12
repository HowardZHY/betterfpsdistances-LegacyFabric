package com.betterfpsdist.config;

import com.betterfpsdist.BetterfpsdistMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import static net.minecraft.client.Minecraft.getMinecraft;

public class Configuration
{
    /**
     * Loaded everywhere, not synced
     */
    public final CommonConfiguration commonConfig = new CommonConfiguration();

    /**
     * Loaded clientside, not synced
     */
    // private final ClientConfiguration clientConfig;
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Builds configuration tree.
     */
    public Configuration()
    {
    }

    public void load()
    {
        final File config = new File(getMinecraft().mcDataDir, "config");
        final File configPath = new File (config, "betterfpsdist.json");

        if (!config.exists())
        {
            BetterfpsdistMod.LOGGER.warn("Config for dynamic view not found, recreating default");
            save();
        }
        else
        {
            try
            {
                commonConfig.deserialize(gson.fromJson(Files.newBufferedReader(configPath.toPath()), JsonObject.class));
            }
            catch (IOException e)
            {
                BetterfpsdistMod.LOGGER.error("Could not read config from:" + configPath, e);
            }
        }
    }

    public void save()
    {
        final File config = new File(getMinecraft().mcDataDir, "config");
        final File configPath = new File (config, "betterfpsdist.json");
        try
        {
            final BufferedWriter writer = Files.newBufferedWriter(configPath.toPath());
            gson.toJson(commonConfig.serialize(), JsonObject.class, writer);
            writer.close();
        }
        catch (IOException e)
        {
            BetterfpsdistMod.LOGGER.error("Could not write config to:" + configPath, e);
        }
    }

    public CommonConfiguration getCommonConfig()
    {
        return commonConfig;
    }
}
