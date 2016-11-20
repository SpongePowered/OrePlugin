package org.spongepowered.ore.client.exception;

/**
 * An exception thrown when a requested plugin cannot be found on the server.
 */
public final class PluginNotFoundException extends OreException {

    public PluginNotFoundException(String pluginId) {
        super("Plugin \"" + pluginId + "\" could not be found.");
    }

}
