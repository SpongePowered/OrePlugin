package org.spongepowered.ore.client;

import org.spongepowered.ore.client.exception.NoUpdateAvailableException;
import org.spongepowered.ore.client.exception.PluginAlreadyInstalledException;
import org.spongepowered.ore.client.exception.PluginNotInstalledException;
import org.spongepowered.ore.client.http.HttpUtils;
import org.spongepowered.ore.model.Project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
     * Returns true if the plugin with the specified ID is installed, loaded
     * or not.
     *
     * @param id Plugin ID
     * @return True if installed
     */
    boolean isInstalled(String id);

    /**
     * Installs a plugin of the specified ID.
     *
     * @param id Plugin ID
     * @param version Plugin version
     */
    void installPlugin(String id, String version) throws IOException, PluginAlreadyInstalledException;

    /**
     * Uninstalls a plugin.
     *
     * @param id ID of plugin to uninstall
     */
    void uninstallPlugin(String id) throws IOException, PluginNotInstalledException;

    /**
     * Returns true if there is an update available for the specified plugin
     * ID.
     *
     * @param id plugin ID
     * @return True if there is an update available
     */
    boolean isUpdateAvailable(String id) throws IOException, PluginNotInstalledException;

    /**
     * Downloads an update for a plugin of the specified ID.
     *
     * @param id Plugin ID
     * @param version Plugin version
     */
    void downloadUpdate(String id, String version)
        throws IOException, PluginNotInstalledException, NoUpdateAvailableException;

    /**
     * Returns true if there are any updates to apply.
     *
     * @return True if there are updates to apply
     */
    boolean hasUninstalledUpdates();

    /**
     * Returns the amount of updates there are to apply.
     *
     * @return Amount of updates to apply
     */
    int getUninstalledUpdates();

    /**
     * Applies all updates that are currently pending.
     */
    void installUpdates() throws IOException;

    /**
     * Returns true if there are uninstallations to complete.
     *
     * @return True if uninstallations to complete
     */
    boolean hasPendingUninstallations();

    /**
     * Returns the amount of plugins to remove.
     *
     * @return Amount to remove
     */
    int getPendingUninstallations();

    /**
     * Deletes pending uninstallations.
     */
    void completeUninstallations() throws IOException;

    /**
     * Searches for {@link Project}s based on the given query.
     *
     * @param query Query for search
     * @return List of projects matching query
     */
    List<Project> searchProjects(String query) throws IOException;

}
