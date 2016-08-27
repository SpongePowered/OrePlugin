package org.spongepowered.ore.client;

import static org.spongepowered.ore.client.Routes.DOWNLOAD;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * Represents a plugin download from the Ore server.
 */
public final class PluginDownload {

    private final OreClient client;
    private final String pluginId, version;
    private String name;
    private InputStream in;

    /**
     * Constructs a new PluginDownload ready to begin.
     *
     * @param client Client object
     * @param pluginId ID of plugin to download
     * @param version Version of plugin to download
     */
    public PluginDownload(OreClient client, String pluginId, String version) {
        this.client = client;
        this.pluginId = pluginId;
        this.version = version;
    }

    /**
     * Returns the {@link InputStream} of the download if the connection has
     * been opened, empty otherwise.
     *
     * @return InputStream of download
     */
    public Optional<InputStream> getInputStream() {
        return Optional.ofNullable(this.in);
    }

    /**
     * Returns the ID of the plugin to download.
     *
     * @return ID of plugin to download
     */
    public String getPluginId() {
        return this.pluginId;
    }

    /**
     * Returns the version of the plugin to download.
     *
     * @return Version of plugin to download
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns the name of the download (file name without extension) if it is
     * available, empty otherwise.
     *
     * @return Name of download
     */
    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    /**
     * Returns the file name of the download if it is available, empty
     * otherwise.
     *
     * @return File name of download
     */
    public Optional<String> getFileName() {
        return getName().map(name -> name + ".jar");
    }

    /**
     * Opens a connection to the server.
     *
     * @throws IOException
     */
    public void openConnection() throws IOException {
        // Establish connection
        URL url = new URL(this.client.getRouteUrl(DOWNLOAD, this.pluginId, this.version));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Get name for file
        String contentDisposition = conn.getHeaderField("Content-Disposition");
        this.name = this.pluginId;
        if (contentDisposition != null) {
            String section = contentDisposition.split(";")[1];
            this.name = section.substring(section.indexOf('"') + 1, section.lastIndexOf('.'));
        }

        this.in = conn.getInputStream();
    }

}
