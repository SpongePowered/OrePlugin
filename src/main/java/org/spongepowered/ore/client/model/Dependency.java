package org.spongepowered.ore.client.model;

/**
 * Represents a {@link Project} {@link Version}'s dependency to another Project
 * Version.
 */
public class Dependency {

    private String pluginId;
    private String version;

    /**
     * Returns the plugin ID of the dependency Project.
     *
     * @return Plugin ID
     */
    public String getPluginId() {
        return this.pluginId;
    }

    /**
     * Returns the version of the Project.
     *
     * @return Project version
     */
    public String getVersion() {
        return this.version;
    }

}
