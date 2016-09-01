package org.spongepowered.ore.client;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.move;
import static org.spongepowered.ore.client.Routes.PROJECT;
import static org.spongepowered.ore.client.Routes.PROJECT_LIST;

import org.apache.commons.io.FileUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.ore.client.exception.NoUpdateAvailableException;
import org.spongepowered.ore.client.exception.PluginAlreadyInstalledException;
import org.spongepowered.ore.client.exception.PluginNotFoundException;
import org.spongepowered.ore.client.exception.PluginNotInstalledException;
import org.spongepowered.ore.client.http.OreConnection;
import org.spongepowered.ore.client.http.PluginDownload;
import org.spongepowered.ore.model.Project;
import org.spongepowered.plugin.meta.PluginMetadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * An implementation of {@link OreClient} built around the {@link Sponge}
 * platform.
 */
public final class SpongeOreClient implements OreClient {

    private final PluginManager pluginManager;
    private final URL rootUrl;
    private final Path modsDir, updatesDir;
    private final Map<String, Installation> newInstalls = new HashMap<>();
    private final Map<String, Installation> updatesToInstall = new HashMap<>();
    private final Set<PluginContainer> toRemove = new HashSet<>();

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
    public boolean isInstalled(String id) {
        // Returns true if the plugin is loaded and is not going to be
        // uninstalled or if the plugin is unloaded and is going to be
        // installed
        boolean loaded = this.pluginManager.isLoaded(id);
        boolean removalPending = this.toRemove.stream()
            .filter(plugin -> plugin.getId().equals(id))
            .findAny()
            .isPresent();
        return (loaded && !removalPending) || (!loaded && this.newInstalls.containsKey(id));
    }

    @Override
    public void installPlugin(String id, String version) throws IOException, PluginAlreadyInstalledException {
        checkNotInstalled(id);

        // A plugin can be uninstalled but still loaded, download to updates
        // dir if this is the case
        Path target;
        if (this.pluginManager.isLoaded(id))
            this.downloadPlugin(id, version, this.updatesDir, this.updatesToInstall);
        else
            this.downloadPlugin(id, version, this.modsDir, this.newInstalls);

        // Remove from uninstallation list if present
        this.toRemove.removeIf(plugin -> plugin.getId().equals(id));
    }

    @Override
    public void uninstallPlugin(String id) throws IOException, PluginNotInstalledException {
        checkInstalled(id);

        // Add to removal set if loaded, delete file otherwise
        if (this.pluginManager.isLoaded(id))
            this.toRemove.add(this.pluginManager.getPlugin(id).get());
        else {
            // Delete pending installs
            delete(this.newInstalls.remove(id).getPath());
        }

        // Delete pending updates
        if (this.updatesToInstall.containsKey(id))
            delete(this.updatesToInstall.remove(id).getPath());
    }

    @Override
    public boolean isUpdateAvailable(String id) throws IOException, PluginNotInstalledException {
        checkInstalled(id);
        Project project = OreConnection.open(this, PROJECT, id).read(Project.class);

        String currentVersion = this.pluginManager.getPlugin(id)
            .<String>map(plugin -> plugin.getVersion().orElse(""))
            .orElse(this.newInstalls.get(id).getVersion());

        return !currentVersion.equals(VERSION_RECOMMENDED)
            && !currentVersion.equals(project.getRecommendedVersion().getName());
    }

    @Override
    public void downloadUpdate(String id, String version)
        throws IOException, PluginNotInstalledException, NoUpdateAvailableException {
        checkInstalled(id);
        if (version.equals(VERSION_RECOMMENDED) && !isUpdateAvailable(id))
            throw new NoUpdateAvailableException(id);
        this.downloadPlugin(id, version, this.updatesDir, this.updatesToInstall);
    }

    @Override
    public boolean hasUninstalledUpdates() {
        return !this.updatesToInstall.isEmpty();
    }

    @Override
    public int getUninstalledUpdates() {
        return this.updatesToInstall.size();
    }

    @Override
    public void installUpdates() throws IOException {
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
            Path updatePath = this.updatesToInstall.get(pluginId).getPath();
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
        return Arrays.asList(OreConnection.openWithQuery(this, PROJECT_LIST, "?q=" + query).read(Project[].class));
    }

    @Override
    public boolean hasPendingUninstallations() {
        return !this.toRemove.isEmpty();
    }

    @Override
    public int getPendingUninstallations() {
        return this.toRemove.size();
    }

    @Override
    public void completeUninstallations() throws IOException {
        // Perform uninstalls
        for (PluginContainer plugin : this.toRemove) {
            Optional<Path> pathOpt = plugin.getSource();
            if (pathOpt.isPresent())
                deleteIfExists(pathOpt.get());
        }
    }

    private Path downloadPlugin(String id, String version, Path targetDir, Map<String, Installation> downloadMap)
        throws IOException {
        // Initialize download
        PluginDownload download;
        try {
            download = new PluginDownload(this, id, version).open();
        } catch (FileNotFoundException e) {
            throw new PluginNotFoundException(id);
        }

        // Override already pending installs/updates
        if (downloadMap.containsKey(id))
            delete(downloadMap.remove(id).getPath());

        // Copy to target file
        Path target = findAvailablePath(download.getName().get(), targetDir.resolve(download.getFileName().get()));
        createDirectories(target.getParent());
        createFile(target);

        copy(download.getInputStream().get(), target, StandardCopyOption.REPLACE_EXISTING);
        downloadMap.put(id, new Installation(id, version, target));
        return target;
    }

    private Path findAvailablePath(String name, Path target) {
        int conflicts = 0;
        while (exists(target))
            target = target.getParent().resolve(name + " (" + ++conflicts + ").jar");
        return target;
    }

    private void checkNotInstalled(String id) throws PluginAlreadyInstalledException {
        if (isInstalled(id))
            throw new PluginAlreadyInstalledException(id);
    }

    private void checkInstalled(String id) throws PluginNotInstalledException {
        if (!isInstalled(id))
            throw new PluginNotInstalledException(id);
    }

}
