package org.spongepowered.ore;

import static org.spongepowered.ore.Routes.DOWNLOAD_RECOMMENDED;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * A simple client for interacting with the Ore web server.
 */
public class OreAPI {

    private final String rootUrl;

    public OreAPI(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    /**
     * Returns the root URL of Ore instance.
     *
     * @return Root URL
     */
    public String getRootUrl() {
        return this.rootUrl;
    }

    /**
     * Returns the full URL of the specified route.
     *
     * @param route Ore route
     * @param params Format parameters
     * @return Full URL
     */
    public String getRouteUrl(String route, Object... params) {
        return this.rootUrl + String.format(route, params);
    }

    /**
     * Downloads a plugin of the specified ID to the specified target
     * directory.
     *
     * @param id Plugin ID
     * @param targetDir Directory to download plugin to
     */
    public void downloadPlugin(String id, Path targetDir) {
        try {
            URL url = new URL(getRouteUrl(DOWNLOAD_RECOMMENDED, id));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            String contentDisposition = conn.getHeaderField("Content-Disposition");
            String fileName = id + ".jar";
            if (contentDisposition != null) {
                String section = contentDisposition.split(";")[1];
                fileName = section.substring(section.indexOf('"') + 1, section.lastIndexOf('"'));
            }

            Path target = targetDir.resolve(fileName);
            if (Files.notExists(target)) {
                Files.createDirectories(target.getParent());
                Files.createFile(target);
            }
            Files.copy(conn.getInputStream(), targetDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
