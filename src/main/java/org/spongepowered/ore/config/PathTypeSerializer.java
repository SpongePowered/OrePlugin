package org.spongepowered.ore.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A {@link TypeSerializer} implementation for {@link Path}s.
 */
public final class PathTypeSerializer implements TypeSerializer<Path> {

    @Override
    public Path deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return Paths.get(value.getString("."));
    }

    @Override
    public void serialize(TypeToken<?> type, Path obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(obj.toString());
    }

}
