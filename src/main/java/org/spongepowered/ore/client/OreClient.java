package org.spongepowered.ore.client;

import org.spongepowered.ore.client.http.HttpUtils;
import org.spongepowered.ore.model.Project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A client for interacting with the OrePlugin web server.
 */
public interface OreClient {

    /**
     * String used for retrieving the recommended version for a plugin.
     */
    String VERSION_RECOMMENDED = "recommended";

    /**
     * Returns the root URL of OrePlugin instance.
     *
     * @return Root URL
     */
    URL getRootUrl();

    /**
     * Returns the full URL of the specified route.
     *
     * @param route OrePlugin route
     * @param queryString Query string to append to URL
     * @param params Format parameters
     * @return Full URL
     */
    default URL getRouteUrl(String route, @Nullable String queryString, Object... params) {
        try {
            return new URL(getRootUrl() + String.format(route, params)
                + HttpUtils.encodeQueryStringParameters(queryString));
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the full URL of the specified route.
     *
     * @param route OrePlugin route
     * @param params Format parameters
     * @return Full URL
     */
    default URL getRouteUrl(String route, Object... params) {
        return getRouteUrl(route, "", params);
    }

    /**
     * Returns the {@link Path} to the directory in which installed plugins
     * reside.
     *
     * @return Path to mods dir
     */
    Path getModsDir();

    /**
     * Returns the {@link Path} to the directory in which pending updates
     * reside.
     *
     * @return Path to updates dir
     */
    Path getUpdatesDir();

    /**
     * Installs a plugin of the specified ID.
     *
     * @param id Plugin ID
     * @param version Plugin version
     */
    void installPlugin(String id, String version);

    /**
     * Uninstalls a plugin.
     *
     * @param id ID of plugin to uninstall
     */
    void uninstallPlugin(String id);

    /**
     * Downloads an update for a plugin of the specified ID.
     *
     * @param id Plugin ID
     * @param version Plugin version
     */
    void downloadUpdate(String id, String version);

    /**
     * Returns true if there are any updates to apply.
     *
     * @return True if there are updates to apply
     */
    boolean hasUpdates();

    /**
     * Returns the amount of updates there are to apply.
     *
     * @return Amount of updates to apply
     */
    int getUpdates();

    /**
     * Applies all updates that are currently pending.
     */
    void applyUpdates() throws IOException;

    /**
     * Returns true if there are uninstallations to complete.
     *
     * @return True if uninstallations to complete
     */
    boolean hasRemovals();

    /**
     * Returns the amount of plugins to remove.
     *
     * @return Amount to remove
     */
    int getRemovals();

    /**
     * Deletes pending uninstallations.
     */
    void applyRemovals() throws IOException;

    /**
     * Searches for {@link Project}s based on the given query.
     *
     * @param query Query for search
     * @return List of projects matching query
     */
    List<Project> searchProjects(String query) throws IOException;

}
