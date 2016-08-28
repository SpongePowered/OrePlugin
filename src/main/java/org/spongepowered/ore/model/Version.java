package org.spongepowered.ore.model;

import com.google.common.base.Objects;

/**
 * Represents a version of a {@link Project}.
 */
public final class Version {

    private Channel channel;
    private String name;

    /**
     * Returns the {@link Channel} this version belongs to.
     *
     * @return Channel version belongs to
     */
    public Channel getChannel() {
        return this.channel;
    }

    /**
     * Returns the version name.
     *
     * @return Version name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("channel", this.channel)
            .add("name", this.name)
            .toString();
    }

}
