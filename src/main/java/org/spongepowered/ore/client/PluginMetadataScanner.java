package org.spongepowered.ore.client;

import static java.nio.file.Files.list;

import org.spongepowered.plugin.meta.McModInfo;
import org.spongepowered.plugin.meta.PluginMetadata;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public final class PluginMetadataScanner {

    private static final String METADATA_NAME = "mcmod.info";
    private final Map<Path, List<PluginMetadata>> metadata = new HashMap<>();
    private final Path targetDir;

    public PluginMetadataScanner(Path targetDir) {
        this.targetDir = targetDir;
    }

    public Map<Path, List<PluginMetadata>> scan() throws IOException {
        List<Path> installedPaths = list(this.targetDir)
            .filter(p -> p.toString().endsWith(".jar"))
            .collect(Collectors.toList());
        for (Path path : installedPaths) {
            scan(path);
        }
        return this.metadata;
    }

    private void scan(Path path) throws IOException {
        JarFile jar = new JarFile(path.toFile());
        ZipEntry metaEntry = jar.getEntry(METADATA_NAME);
        if (metaEntry != null)
            this.metadata.put(path, McModInfo.DEFAULT.read(jar.getInputStream(metaEntry)));
    }

}
