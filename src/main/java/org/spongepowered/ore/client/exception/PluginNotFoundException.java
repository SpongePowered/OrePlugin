package org.spongepowered.ore.client.exception;

public final class PluginNotFoundException extends OreException {

    public PluginNotFoundException(String pluginId) {
        super("Plugin \"" + pluginId + "\" could not be found.");
    }

}
