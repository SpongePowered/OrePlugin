package org.spongepowered.ore;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.config.OreConfig;

/**
 * Represents an installation of an {@link OreClient} on some server.
 */
public interface OrePlugin {

    /**
     * Returns the client to use for interacting with the web API.
     *
     * @return Client to interact with API
     */
    OreClient getClient();

    /**
     * Loads (or reloads) the configuration file.
     *
     * @return true if successful
     */
    boolean loadConfig();

    /**
     * Returns the loaded {@link OreConfig} for this plugin.
     *
     * @return OreConfig instance
     */
    ConfigurationNode getConfigRoot();

}
