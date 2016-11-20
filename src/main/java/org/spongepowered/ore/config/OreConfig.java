package org.spongepowered.ore.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.asset.Asset;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.*;

/**
 * Handles configuration management for the plugin.
 */
public final class OreConfig {

    private static final TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();
    private static ConfigurationOptions options = ConfigurationOptions.defaults();

    static {
        serializers.registerType(TypeToken.of(Path.class), new PathTypeSerializer());
        options = options.setSerializers(serializers);
    }

    private ConfigurationNode root;

    /**
     * Loads the config into a root node at the specified {@link Path} with
     * the specified {@link Asset} default config.
     *
     * @param path Path to load from
     * @param defaultConfig Default config
     * @return This instance
     * @throws IOException
     */
    public OreConfig load(Path path, Asset defaultConfig) throws IOException {
        if (notExists(path)) {
            createDirectories(path.getParent());
            defaultConfig.copyToFile(path);
        }
        this.root = createLoader(path).load();
        return this;
    }

    /**
     * Saves the root node at the specified {@link Path}.
     *
     * @param path Path to save to
     * @return This instance
     * @throws IOException
     */
    public OreConfig save(Path path) throws IOException {
        if (notExists(path)) {
            createDirectories(path.getParent());
            createFile(path);
        }
        createLoader(path).save(this.root);
        return this;
    }

    private ConfigurationLoader<CommentedConfigurationNode> createLoader(Path path) {
        return HoconConfigurationLoader.builder().setPath(path).setDefaultOptions(options).build();
    }

    /**
     * Returns the root {@link ConfigurationNode} of thie config.
     *
     * @return Root node
     */
    public ConfigurationNode getRoot() {
        if (this.root == null)
            throw new IllegalStateException("config not loaded");
        return this.root;
    }

}
