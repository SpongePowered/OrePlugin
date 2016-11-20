package org.spongepowered.ore.client.exception;

/**
 * A base exception to raise within the plugin.
 */
public class OreException extends RuntimeException {

    protected OreException(String message) {
        super(message, null, false, false);
    }

}
