package org.spongepowered.ore.client;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple client for interacting with the OrePlugin web server.
 */
public class OreClient {

    public static final String VERSION_RECOMMENDED = "recommended";

    private final String rootUrl;
    private final Map<String, Path> installsAwaitingRestart = new HashMap<>();

    /**
     * Constructs a new instance of the client pointed at the specified URL.
     *
     * @param rootUrl OrePlugin instance URL
     */
    public OreClient(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    /**
     * Returns the root URL of OrePlugin instance.
     *
     * @return Root URL
     */
    public String getRootUrl() {
        return this.rootUrl;
    }

    /**
     * Returns the full URL of the specified route.
     *
     * @param route OrePlugin route
     * @param params Format parameters
     * @return Full URL
     */
    public String getRouteUrl(String route, Object... params) {
        return this.rootUrl + String.format(route, params);
    }

    /**
     * Returns a pluginId -> install path map of plugins that have been
     * download but are not yet active.
     *
     * @return Installs awaiting restart
     */
    public Map<String, Path> getInstallsAwaitingRestart() {
        return ImmutableMap.copyOf(this.installsAwaitingRestart);
    }

    /**
     * Installs a plugin of the specified ID to the specified target
     * directory.
     *
     * @param id Plugin ID
     * @param version Plugin version
     * @param targetDir Directory to download plugin to
     */
    public void installPlugin(String id, String version, Path targetDir) {
        try {
            if (Sponge.getPluginManager().isLoaded(id))
                throw new RuntimeException("Plugin \"" + id + "\" is already installed.");

            // Establish connection
            URL url = new URL(getRouteUrl(Routes.DOWNLOAD, id, version));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Get name for file
            String contentDisposition = conn.getHeaderField("Content-Disposition");
            String name = id;
            if (contentDisposition != null) {
                String section = contentDisposition.split(";")[1];
                name = section.substring(section.indexOf('"') + 1, section.lastIndexOf('.'));
            }

            if (this.installsAwaitingRestart.containsKey(id))
                delete(this.installsAwaitingRestart.remove(id));

            // Find available name
            Path target = targetDir.resolve(name + ".jar");
            int conflicts = 0;
            while (exists(target))
                target = targetDir.resolve(name + " (" + ++conflicts + ").jar");

            // Create file
            createDirectories(target.getParent());
            createFile(target);

            // Download
            copy(conn.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            this.installsAwaitingRestart.put(id, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
