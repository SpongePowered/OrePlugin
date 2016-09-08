package org.spongepowered.ore.client.exception;

public final class OreConnectException extends OreException {

    public OreConnectException(String url) {
        super("Could not connect to Ore repository at URL: " + url);
    }

}
