package org.spongepowered.ore.client.exception;

import java.io.FileNotFoundException;

public class PluginNotFoundException extends Exception {

    public PluginNotFoundException(String pluginId, FileNotFoundException cause) {
        super("Plugin \"" + pluginId + "\" could not be found.", cause);
    }

}
