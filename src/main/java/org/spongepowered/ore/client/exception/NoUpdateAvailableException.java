package org.spongepowered.ore.client.exception;

public final class NoUpdateAvailableException extends OreException {

    public NoUpdateAvailableException(String pluginId) {
        super("No update available for Plugin \"" + pluginId + "\".");
    }

}
