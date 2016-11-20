package org.spongepowered.ore.client.model.project;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a {@link Project} category on Ore.
 */
public final class Category {

    private String title;
    @SerializedName("icon")
    private String iconName;

    /**
     * Returns the title of the category.
     *
     * @return Category title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the icon name of the category.
     *
     * @return Icon name
     */
    public String getIconName() {
        return this.iconName;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("title", this.title)
            .add("icon", this.iconName)
            .toString();
    }

}
