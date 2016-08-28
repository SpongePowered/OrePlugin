package org.spongepowered.ore.client.exception;

public class NoUpdateAvailableException extends Exception {

    public NoUpdateAvailableException(String pluginId) {
        super("No update available for Plugin \"" + pluginId + "\".");
    }

}
