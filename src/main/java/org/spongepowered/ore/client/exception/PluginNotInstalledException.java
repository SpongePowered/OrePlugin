package org.spongepowered.ore.client.exception;

public final class PluginNotInstalledException extends OreException {

    public PluginNotInstalledException(String id) {
        super("Plugin \"" + id + "\" is not installed.");
    }

}
