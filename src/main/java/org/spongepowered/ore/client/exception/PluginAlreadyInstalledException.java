package org.spongepowered.ore.client.exception;

public class PluginAlreadyInstalledException extends Exception {

    public PluginAlreadyInstalledException(String id) {
        super("Plugin \"" + id + "\" is already installed.");
    }

}
