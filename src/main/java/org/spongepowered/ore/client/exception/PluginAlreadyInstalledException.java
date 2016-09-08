package org.spongepowered.ore.client.exception;

public final class PluginAlreadyInstalledException extends OreException {

    public PluginAlreadyInstalledException(String id) {
        super("Plugin \"" + id + "\" is already installed.");
    }

}
