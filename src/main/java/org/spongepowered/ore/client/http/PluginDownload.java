package org.spongepowered.ore.client.http;

import static org.spongepowered.ore.client.Routes.DOWNLOAD;

import org.spongepowered.ore.client.OreClient;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a plugin download from the Ore server.
 */
public final class PluginDownload extends OreConnection {

    private final String pluginId, version;
    private String name;

    /**
     * Constructs a new PluginDownload ready to begin.
     *
     * @param client Client object
     * @param pluginId ID of plugin to download
     * @param version Version of plugin to download
     */
    public PluginDownload(OreClient client, String pluginId, String version) {
        super(client, DOWNLOAD, (Object) pluginId, version);
        this.pluginId = pluginId;
        this.version = version;
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
        super.openConnection();
        // Get name for file
        String contentDisposition = this.http.getHeaderField("Content-Disposition");
        this.name = this.pluginId;
        if (contentDisposition != null) {
            String section = contentDisposition.split(";")[1];
            this.name = section.substring(section.indexOf('"') + 1, section.lastIndexOf('.'));
        }
    }

}
