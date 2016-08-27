package org.spongepowered.ore.client;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;

import org.spongepowered.api.plugin.PluginManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class SpongeOreClient implements OreClient {

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
    public void updatePlugin(String id, String version) {
        if (!this.pluginManager.isLoaded(id))
            throw new RuntimeException("Plugin \"" + id + "\" is not installed.");
        this.downloadPlugin(id, version, this.updatesAwaitingRestart, this.updatesDir);
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
            int conflicts = 0;
            while (exists(target))
                target = targetDir.resolve(download.getName().get() + " (" + ++conflicts + ").jar");

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

}
