package org.spongepowered.ore.client.exception;

public class PluginNotInstalledException extends Exception {

    public PluginNotInstalledException(String id) {
        super("Plugin \"" + id + "\" is not installed.");
    }

}
