package org.spongepowered.ore.client.exception;

/**
 * Exception thrown when a requested plugin does not support the current
 * platform API version that the server is currently running.
 */
public final class UnsupportedPlatformVersion extends OreException {

    private final String required;
    private final String current;

    public UnsupportedPlatformVersion(String required, String current) {
        super("Unsupported platform API major version! (required: " + required + ", current: " + current + ")");
        this.required = required;
        this.current = current;
    }

    /**
     * Returns the required version string.
     *
     * @return Required version string
     */
    public String getRequired() {
        return required;
    }

    /**
     * Returns the server's current version string.
     *
     * @return Current version string.
     */
    public String getCurrent() {
        return current;
    }

}
