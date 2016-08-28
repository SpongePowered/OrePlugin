package org.spongepowered.ore.model;

import com.google.common.base.Objects;

public final class Category {

    private String title;
    private String icon;

    public String getTitle() {
        return this.title;
    }

    public String getIcon() {
        return this.icon;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("title", this.title)
            .add("icon", this.icon)
            .toString();
    }

}
