package org.spongepowered.ore.client;

import java.nio.file.Path;

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
    String getRootUrl();

    /**
     * Returns the full URL of the specified route.
     *
     * @param route OrePlugin route
     * @param params Format parameters
     * @return Full URL
     */
    default String getRouteUrl(String route, Object... params) {
        return getRootUrl() + String.format(route, params);
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
    int updateCount();

    /**
     * Applies all updates that are currently pending.
     */
    void applyUpdates();

}
