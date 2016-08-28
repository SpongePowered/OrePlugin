package org.spongepowered.ore.model;

import com.google.common.base.Objects;

public final class Channel {

    private String name;
    private String color;

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("name", this.name)
            .add("color", this.color)
            .toString();
    }

}
