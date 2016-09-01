package org.spongepowered.ore.config;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.notExists;
import static java.nio.file.Files.write;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ore configuration options.
 */
@SuppressWarnings("FieldCanBeLocal")
public final class OreConfig {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter())
        .create();

    private String repositoryUrl = "http://ore-staging.spongepowered.org";
    private Path installationDirectory = Paths.get("./mods");
    private Path updatesDirectory = Paths.get("./updates");

    /**
     * Returns the location of the Ore server instance to use.
     *
     * @return Ore instance location
     */
    public String getRepositoryUrl() {
        return this.repositoryUrl;
    }

    /**
     * Returns the directory in which mods/plugins are kept.
     *
     * @return Mods directory
     */
    public Path getInstallationDirectory() {
        return this.installationDirectory;
    }

    /**
     * Returns the directory in which Ore should store downloaded updates
     * temporarily.
     *
     * @return Updates directory
     */
    public Path getUpdatesDirectory() {
        return this.updatesDirectory;
    }

    /**
     * Loads a new {@link OreConfig} from disk at the specified {@link Path} or
     * creates one if none exists.
     *
     * @param path Path to load from
     * @return Newly loaded OreConfig
     * @throws IOException
     */
    public static OreConfig load(Path path) throws IOException {
        if (notExists(path)) {
            createDirectories(path.getParent());
            createFile(path);
            OreConfig defaultConfig = new OreConfig();
            write(path, (GSON.toJson(defaultConfig) + '\n').getBytes());
            return defaultConfig;
        }
        return GSON.fromJson(new InputStreamReader(newInputStream(path)), OreConfig.class);
    }

}
