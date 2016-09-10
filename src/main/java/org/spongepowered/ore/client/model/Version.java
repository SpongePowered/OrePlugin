package org.spongepowered.ore.client.model;

import com.google.common.base.Objects;

import java.util.List;

/**
 * Represents a version of a {@link Project}.
 */
public final class Version {

    private int id;
    private String createdAt;
    private String name;
    private List<Dependency> dependencies;
    private String pluginId;
    private Channel channel;
    private long fileSize;

    /**
     * Returns this Version's unique ID.
     *
     * @return Unique ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns a date string of creation.
     *
     * @return Creation date
     */
    public String getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Returns the version name.
     *
     * @return Version name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a list of {@link Dependency} that this version has.
     *
     * @return List of dependencies
     */
    public List<Dependency> getDependencies() {
        return this.dependencies;
    }

    /**
     * Returns the plugin ID of this version.
     *
     * @return Plugin ID
     */
    public String getPluginId() {
        return this.pluginId;
    }

    /**
     * Returns the {@link Channel} this version belongs to.
     *
     * @return Channel version belongs to
     */
    public Channel getChannel() {
        return this.channel;
    }

    /**
     * Returns the file size of this version's download file.
     *
     * @return Size of download
     */
    public long getFileSize() {
        return this.fileSize;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("channel", this.channel)
            .add("name", this.name)
            .toString();
    }

}
