package org.spongepowered.ore.client;

import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.ore.client.exception.NoUpdateAvailableException;
import org.spongepowered.ore.client.exception.PluginAlreadyInstalledException;
import org.spongepowered.ore.client.exception.PluginNotFoundException;
import org.spongepowered.ore.client.exception.PluginNotInstalledException;
import org.spongepowered.ore.client.http.HttpUtils;
import org.spongepowered.ore.client.model.Project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * Returns an {@link Installation} of the specified plugin ID if it exists.
     *
     * @param id Plugin ID
     * @return Plugin installation
     */
    Optional<Installation> getInstallation(String id);

    /**
     * Installs a plugin of the specified ID.
     *
     * @param id Plugin ID
     * @param version Plugin version
     * @throws IOException
     * @throws PluginAlreadyInstalledException if a plugin with the specified
     *         ID is already installed
     * @throws PluginNotFoundException if a plugin with the specified ID
     *         cannot be found on Ore
     */
    void installPlugin(String id, String version)
        throws IOException, PluginAlreadyInstalledException, PluginNotFoundException;

    /**
     * Uninstalls a plugin.
     *
     * @param id ID of plugin to uninstall
     * @throws IOException
     * @throws PluginNotInstalledException if there is no plugin with the
     *         specified ID installed
     */
    void uninstallPlugin(String id) throws IOException, PluginNotInstalledException;

    /**
     * Returns true if there is an update available for the specified plugin
     * ID.
     *
     * @param id plugin ID
     * @return True if there is an update available
     * @throws IOException
     * @throws PluginNotInstalledException if there is no plugin installed
     *         with the specified ID
     */
    boolean isUpdateAvailable(String id) throws IOException, PluginNotInstalledException;

    /**
     * Returns any available updates among loaded plugins.
     *
     * @return A map of plugin ID -> new version
     * @throws IOException
     */
    Map<PluginContainer, String> getAvailableUpdates() throws IOException;

    /**
     * Downloads an update for a plugin of the specified ID.
     *
     * @param id Plugin ID
     * @param version Plugin version
     * @throws IOException
     * @throws PluginNotInstalledException if there is no plugin with the
     *         specified ID installed
     * @throws PluginNotFoundException if there is no plugin on Ore with the
     *         specified ID
     * @throws NoUpdateAvailableException if there is no update available for
     *         the specified plugin on Ore
     */
    void downloadUpdate(String id, String version)
        throws IOException, PluginNotInstalledException, PluginNotFoundException, NoUpdateAvailableException;

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
     * @throws IOException
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
     * @throws IOException
     */
    void completeUninstallations() throws IOException;

    /**
     * Retrieves and returns a {@link Project} of the specified ID.
     *
     * @return Project if exists, empty otherwise
     * @throws IOException
     */
    Optional<Project> getProject(String id) throws IOException;

    /**
     * Searches for {@link Project}s based on the given query.
     *
     * @param query Query for search
     * @return List of projects matching query
     * @throws IOException
     */
    List<Project> searchProjects(String query) throws IOException;

}
