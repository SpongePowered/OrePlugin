package org.spongepowered.ore.client.model;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a release channel for a {@link Project} (i.e. "Beta", "Alpha",
 * etc).
 */
public final class Channel {

    private String name;
    @SerializedName("color")
    private String colorHex;

    /**
     * Returns the name of the channel.
     *
     * @return Name of channel
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the color hex of the channel.
     *
     * @return Color hex
     */
    public String getColorHex() {
        return this.colorHex;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("name", this.name)
            .add("color", this.colorHex)
            .toString();
    }

}
