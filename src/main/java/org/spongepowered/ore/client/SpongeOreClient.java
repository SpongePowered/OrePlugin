package org.spongepowered.ore.client;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.move;

import org.apache.commons.io.FileUtils;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.plugin.meta.PluginMetadata;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpongeOreClient implements OreClient {

    private final PluginManager pluginManager;
    private final String rootUrl;
    private final Path modsDir, updatesDir;
    private final Map<String, Path> installsAwaitingRestart = new HashMap<>();
    private final Map<String, Path> updatesAwaitingRestart = new HashMap<>();

    public SpongeOreClient(String rootUrl, Path modsDir, Path updatesDir, PluginManager pluginManager) {
        this.rootUrl = rootUrl;
        this.modsDir = modsDir;
        this.updatesDir = updatesDir;
        this.pluginManager = pluginManager;
    }

    @Override
    public String getRootUrl() {
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
        this.downloadPlugin(id, version, this.installsAwaitingRestart, this.modsDir);
    }

    @Override
    public void downloadUpdate(String id, String version) {
        if (!this.pluginManager.isLoaded(id))
            throw new RuntimeException("Plugin \"" + id + "\" is not installed.");
        this.downloadPlugin(id, version, this.updatesAwaitingRestart, this.updatesDir);
    }

    @Override
    public boolean hasUpdates() {
        return !this.updatesAwaitingRestart.isEmpty();
    }

    @Override
    public int updateCount() {
        return this.updatesAwaitingRestart.size();
    }

    @Override
    public void applyUpdates() {
        try {
            Map<Path, List<PluginMetadata>> installedMetadata = new PluginMetadataScanner(this.modsDir).scan();
            for (String pluginId : this.updatesAwaitingRestart.keySet()) {
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
                Path updatePath = this.updatesAwaitingRestart.get(pluginId);
                Path target = this.modsDir.resolve(updatePath.getFileName());
                String fileName = target.getFileName().toString();
                String name = fileName.substring(0, fileName.lastIndexOf('.'));
                target = findAvailablePath(name, target);

                createDirectories(this.modsDir);
                move(updatePath, target);
            }

            FileUtils.cleanDirectory(this.updatesDir.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
