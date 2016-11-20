package org.spongepowered.ore.client.model.user;

import com.google.gson.annotations.SerializedName;
import org.spongepowered.ore.client.model.project.Project;

import java.util.List;

/**
 * Represents an Ore user.
 */
public final class User {

    private int id;
    private String createdAt;
    private String username;
    private List<String> roles;
    @SerializedName("starred")
    private List<String> starredPluginIds;
    private List<Project> projects;

    /**
     * Returns this user's unique ID
     *
     * @return User's unique ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns a date string of when this user was created.
     *
     * @return Date string
     */
    public String getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Returns this user's username.
     *
     * @return Username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns a list of titles this user has.
     *
     * @return Titles user has
     */
    public List<String> getRoles() {
        return this.roles;
    }

    /**
     * Returns a list of plugin IDs that this user has "starred".
     *
     * @return Starred plugin IDs
     */
    public List<String> getStarredPluginIds() {
        return this.starredPluginIds;
    }

    /**
     * Returns a list of {@link Project}s that this user owns.
     *
     * @return List of projects owned by this user
     */
    public List<Project> getProjects() {
        return this.projects;
    }

}
