package org.spongepowered.ore.model;

import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.YELLOW;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;

import java.util.List;

/**
 * Represents a Project on Ore.
 */
public final class Project implements TextRepresentable {

    private String pluginId;
    private String createdAt;
    private String name;
    @SerializedName("owner")
    private String ownerName;
    private String description;
    @SerializedName("href")
    private String homepage;
    private List<ProjectMember> members;
    private List<Channel> channels;
    @SerializedName("recommended")
    private Version recommendedVersion;
    private Category category;
    private int views;
    private int downloads;
    private int stars;

    /**
     * Returns the project's unique plugin ID.
     *
     * @return Unique plugin ID
     */
    public String getPluginId() {
        return this.pluginId;
    }

    /**
     * Returns the date string of creation.
     *
     * @return Date string of creation
     */
    public String getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Returns the name of this project.
     *
     * @return Name of project
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the username of the owner of this project.
     *
     * @return Username of owner of project
     */
    public String getOwnerName() {
        return this.ownerName;
    }

    /**
     * Returns a description of the project.
     *
     * @return Project description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the reference to the project's homepage.
     *
     * @return Reference to homepage
     */
    public String getHomepage() {
        return this.homepage;
    }

    /**
     * Returns a list of {@link ProjectMember}s that belong to this project.
     *
     * @return List of project members
     */
    public List<ProjectMember> getMembers() {
        return this.members;
    }

    /**
     * Returns a list of release {@link Channel}s in this project.
     *
     * @return list of channels
     */
    public List<Channel> getChannels() {
        return this.channels;
    }

    /**
     * Returns the current recommended {@link Version} for this project.
     *
     * @return Recommended version
     */
    public Version getRecommendedVersion() {
        return this.recommendedVersion;
    }

    /**
     * Returns this project's category.
     *
     * @return Project category
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Returns the amount of views this project has.
     *
     * @return Amount of views project has
     */
    public int getViews() {
        return this.views;
    }

    /**
     * Returns the amount of downloads this project has.
     *
     * @return Amount of downloads project has
     */
    public int getDownloads() {
        return this.downloads;
    }

    /**
     * Returns the amount of stars this project has.
     *
     * @return Amount of stars project has
     */
    public int getStars() {
        return this.stars;
    }

    @Override
    public Text toText() {
        return Text.of(YELLOW, this.pluginId + " " + this.recommendedVersion.getName(), GRAY, " by " + this.ownerName);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pluginId", this.pluginId)
            .add("createdAt", this.createdAt)
            .add("name", this.name)
            .add("ownerName", this.ownerName)
            .add("description", this.description)
            .add("href", this.homepage)
            .add("members", this.members)
            .add("channels", this.channels)
            .add("recommendedVersion", this.recommendedVersion)
            .add("category", this.category)
            .add("views", this.views)
            .add("downloads", this.downloads)
            .add("stars", this.stars)
            .toString();
    }

}
