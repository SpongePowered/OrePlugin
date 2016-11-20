package org.spongepowered.ore.client;

import com.google.common.base.Objects;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;

/**
 * Represents an unloaded plugin installation.
 */
public final class Installation {

    private final String pluginId;
    private final String version;
    private final Path path;

    /**
     * Constructs a new installation object for the given plugin ID, version,
     * and the current {@link Path} in which the plugin file resides.
     *
     * @param pluginId Plugin ID
     * @param version Installed version
     * @param path Current path
     */
    public Installation(String pluginId, String version, Path path) {
        this.pluginId = pluginId;
        this.version = version;
        this.path = path;
    }

    /**
     * Returns the plugin ID of this installation.
     *
     * @return Plugin ID
     */
    public String getPluginId() {
        return this.pluginId;
    }

    /**
     * Returns the plugin version of this installation.
     *
     * @return Plugin version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns the {@link Path} in which this plugin's file current resides in.
     *
     * @return Plugin file path
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Constructs a new Installation object from the specified
     * {@link PluginContainer}.
     *
     * @param container Plugin container
     * @return Installation object
     */
    public static Installation fromContainer(PluginContainer container) {
        return new Installation(container.getId(), container.getVersion().orElse("Unknown"),
            container.getSource().orElse(null));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pluginId", this.pluginId)
            .add("version", this.version)
            .add("path", this.path)
            .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.pluginId);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Installation && ((Installation) that).pluginId.equals(this.pluginId);
    }

}
