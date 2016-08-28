package org.spongepowered.ore.model;

import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.YELLOW;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;

import java.util.List;

public final class Project implements TextRepresentable {

    private String pluginId;
    private String createdAt;
    private String name;
    @SerializedName("owner")
    private String ownerName;
    private String description;
    private String href;
    private List<ProjectMember> members;
    private List<Channel> channels;
    @SerializedName("recommended")
    private Version recommendedVersion;
    private Category category;
    private int views;
    private int downloads;
    private int stars;

    public String getPluginId() {
        return this.pluginId;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getName() {
        return this.name;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHref() {
        return this.href;
    }

    public List<ProjectMember> getMembers() {
        return this.members;
    }

    public List<Channel> getChannels() {
        return this.channels;
    }

    public Version getRecommendedVersion() {
        return this.recommendedVersion;
    }

    public Category getCategory() {
        return this.category;
    }

    public int getViews() {
        return this.views;
    }

    public int getDownloads() {
        return this.downloads;
    }

    public int getStars() {
        return this.stars;
    }

    @Override
    public Text toText() {
        return Text.of(YELLOW, this.name + " " + this.recommendedVersion.getName(), GRAY, " by " + this.ownerName);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pluginId", this.pluginId)
            .add("createdAt", this.createdAt)
            .add("name", this.name)
            .add("ownerName", this.ownerName)
            .add("description", this.description)
            .add("href", this.href)
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
