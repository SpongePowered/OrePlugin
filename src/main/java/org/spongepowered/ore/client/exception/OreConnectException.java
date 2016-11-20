package org.spongepowered.ore.client.exception;

/**
 * Exception thrown when there is an issue connecting to the server.
 */
public final class OreConnectException extends OreException {

    public OreConnectException(String url) {
        super("Could not connect to Ore repository at URL: " + url);
    }

}
