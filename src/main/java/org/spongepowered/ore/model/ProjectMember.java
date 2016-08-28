package org.spongepowered.ore.model;

import com.google.common.base.Objects;

import java.util.List;

public final class ProjectMember {

    private int userId;
    private String name;
    private List<String> roles;
    private String headRole;

    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public String getHeadRole() {
        return this.headRole;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("userId", this.userId)
            .add("name", this.name)
            .add("roles", this.roles)
            .add("headRole", this.headRole)
            .toString();
    }

}
