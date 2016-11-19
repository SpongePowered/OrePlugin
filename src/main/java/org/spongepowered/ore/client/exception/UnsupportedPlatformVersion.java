package org.spongepowered.ore.client.exception;

public class UnsupportedPlatformVersion extends OreException {

    private final String required;
    private final String current;

    public UnsupportedPlatformVersion(String required, String current) {
        super("Unsupported platform API major version! (required: " + required + ", current: " + current + ")");
        this.required = required;
        this.current = current;
    }

    public String getRequired() {
        return required;
    }

    public String getCurrent() {
        return current;
    }

}
