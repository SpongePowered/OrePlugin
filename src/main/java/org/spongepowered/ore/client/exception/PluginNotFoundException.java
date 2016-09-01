package org.spongepowered.ore.client.exception;

import java.io.FileNotFoundException;

public class PluginNotFoundException extends FileNotFoundException {

    public PluginNotFoundException(String pluginId) {
        super("Plugin \"" + pluginId + "\" could not be found.");
    }

}
