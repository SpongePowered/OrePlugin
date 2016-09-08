package org.spongepowered.ore.client.exception;

public class OreException extends RuntimeException {

    protected OreException(String message) {
        super(message, null, false, false);
    }

}
