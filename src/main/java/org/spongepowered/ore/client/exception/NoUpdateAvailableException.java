package org.spongepowered.ore.client.exception;

/**
 * Exception thrown when a plugin has no update available.
 */
public final class NoUpdateAvailableException extends OreException {

    public NoUpdateAvailableException(String pluginId) {
        super("No update available for Plugin \"" + pluginId + "\".");
    }

}
