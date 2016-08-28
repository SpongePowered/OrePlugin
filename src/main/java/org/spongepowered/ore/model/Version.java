package org.spongepowered.ore.model;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

public final class Version {

    @SerializedName("channel")
    private String channelName;
    @SerializedName("version")
    private String name;

    public String getChannelName() {
        return this.channelName;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("channelName", this.channelName)
            .add("name", this.name)
            .toString();
    }

}
