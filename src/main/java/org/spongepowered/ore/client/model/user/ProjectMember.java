package org.spongepowered.ore.client.model.user;

import com.google.common.base.Objects;
import org.spongepowered.ore.client.model.project.Project;

import java.util.List;

import javax.management.relation.Role;

/**
 * Represents a user member of a {@link Project}.
 */
public final class ProjectMember {

    private int userId;
    private String name;
    private List<String> roles;
    private String headRole;

    /**
     * Returns the unique ID of the user.
     *
     * @return User unique ID
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Returns the user's username.
     *
     * @return User's username
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a list of {@link Role}s that this member possesses for the
     * {@link Project}.
     *
     * @return Project roles
     */
    public List<String> getRoles() {
        return this.roles;
    }

    /**
     * Returns the top {@link Role} for this member.
     *
     * @return Top role
     */
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
