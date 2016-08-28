package org.spongepowered.ore.client;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.move;
import static org.spongepowered.ore.client.Routes.PROJECT_LIST;

import org.apache.commons.io.FileUtils;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.ore.client.http.OreConnection;
import org.spongepowered.ore.client.http.PluginDownload;
import org.spongepowered.ore.model.Project;
import org.spongepowered.plugin.meta.PluginMetadata;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class SpongeOreClient implements OreClient {

    private final PluginManager pluginManager;
    private final URL rootUrl;
    private final Path modsDir, updatesDir;
    private final Map<String, Path> newInstalls = new HashMap<>();
    private final Map<String, Path> updatesToInstall = new HashMap<>();
    private final List<PluginContainer> toRemove = new ArrayList<>();

    public SpongeOreClient(String rootUrl, Path modsDir, Path updatesDir, PluginManager pluginManager)
        throws MalformedURLException {
        this.rootUrl = new URL(rootUrl);
        this.modsDir = modsDir;
        this.updatesDir = updatesDir;
        this.pluginManager = pluginManager;
    }

    @Override
    public URL getRootUrl() {
        return this.rootUrl;
    }

    @Override
    public Path getModsDir() {
        return this.modsDir;
    }

    @Override
    public Path getUpdatesDir() {
        return this.updatesDir;
    }

    @Override
    public void installPlugin(String id, String version) {
        if (this.pluginManager.isLoaded(id))
            throw new RuntimeException("Plugin \"" + id + "\" is already installed.");
        this.downloadPlugin(id, version, this.newInstalls, this.modsDir);
    }

    @Override
    public void uninstallPlugin(String id) {
        this.toRemove.add(this.pluginManager.getPlugin(id)
            .orElseThrow(() -> new RuntimeException("Plugin \"" + id + "\" is not installed.")));
    }

    @Override
    public void downloadUpdate(String id, String version) {
        if (!this.pluginManager.isLoaded(id))
            throw new RuntimeException("Plugin \"" + id + "\" is not installed.");
        this.downloadPlugin(id, version, this.updatesToInstall, this.updatesDir);
    }

    @Override
    public boolean hasUpdates() {
        return !this.updatesToInstall.isEmpty();
    }

    @Override
    public int getUpdates() {
        return this.updatesToInstall.size();
    }

    @Override
    public void applyUpdates() throws IOException {
        Map<Path, List<PluginMetadata>> installedMetadata = new PluginMetadataScanner(this.modsDir).scan();
        for (String pluginId : this.updatesToInstall.keySet()) {
            // Delete obsolete version
            for (Path installedPath : installedMetadata.keySet()) {
                boolean match = installedMetadata.get(installedPath).stream()
                    .filter(meta -> meta.getId().equals(pluginId))
                    .findAny()
                    .isPresent();
                if (match) {
                    delete(installedPath);
                    break;
                }
            }

            // Install new update
            Path updatePath = this.updatesToInstall.get(pluginId);
            Path target = this.modsDir.resolve(updatePath.getFileName());
            String fileName = target.getFileName().toString();
            String name = fileName.substring(0, fileName.lastIndexOf('.'));
            target = findAvailablePath(name, target);

            createDirectories(this.modsDir);
            move(updatePath, target);
        }

        FileUtils.cleanDirectory(this.updatesDir.toFile());
    }

    @Override
    public List<Project> searchProjects(String query) throws IOException {
        OreConnection conn = new OreConnection(this, PROJECT_LIST, "?q=" + query);
        conn.openConnection();
        return Arrays.asList(conn.read(Project[].class));
    }

    /**
     * Returns true if there are uninstallations to complete.
     *
     * @return True if uninstallations to complete
     */
    public boolean hasRemovalsToFinish() {
        return !this.toRemove.isEmpty();
    }

    /**
     * Returns the amount of plugins to remove.
     *
     * @return Amount to remove
     */
    public int toRemove() {
        return this.toRemove.size();
    }

    /**
     * Deletes pending uninstallations.
     */
    public void finishRemovals() throws IOException {
        // Perform uninstalls
        for (PluginContainer plugin : this.toRemove) {
            Optional<Path> pathOpt = plugin.getSource();
            if (pathOpt.isPresent())
                deleteIfExists(pathOpt.get());
        }
    }

    private void downloadPlugin(String id, String version, Map<String, Path> awaitingRestart, Path targetDir) {
        try {
            // Initialize download
            PluginDownload download = new PluginDownload(this, id, version);
            download.openConnection();

            // Override previous pending installs/updates
            if (awaitingRestart.containsKey(id))
                delete(awaitingRestart.remove(id));

            // Find available name
            Path target = targetDir.resolve(download.getFileName().get());
            target = findAvailablePath(download.getName().get(), target);

            // Create file
            createDirectories(target.getParent());
            createFile(target);

            // Download
            copy(download.getInputStream().get(), target, StandardCopyOption.REPLACE_EXISTING);
            awaitingRestart.put(id, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path findAvailablePath(String name, Path target) {
        int conflicts = 0;
        while (exists(target))
            target = target.getParent().resolve(name + " (" + ++conflicts + ").jar");
        return target;
    }

}
