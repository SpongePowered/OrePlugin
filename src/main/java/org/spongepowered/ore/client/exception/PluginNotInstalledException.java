package org.spongepowered.ore.client.exception;

/**
 * Exception thrown when a plugin that is expected to be installed is not
 * installed.
 */
public final class PluginNotInstalledException extends OreException {

    public PluginNotInstalledException(String id) {
        super("Plugin \"" + id + "\" is not installed.");
    }

}
