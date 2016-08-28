package org.spongepowered.ore.text;

import static org.spongepowered.api.text.Text.of;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.api.text.format.TextStyles.BOLD;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.client.exception.PluginNotInstalledException;
import org.spongepowered.ore.model.Project;

import java.io.IOException;
import java.util.Map;

/**
 * Helper class that contains utilities regarding {@link Text}.
 */
public class TextUtils {

    /**
     * Maps the specified pluginId to the key "pluginId".
     *
     * @param pluginId Plugin ID
     * @return Map
     */
    public static Map<String, TextElement> tuplePid(String pluginId) {
        return ImmutableMap.of("pluginId", of(pluginId));
    }

    /**
     * Returns a {@link Text} describing a {@link Project} for use in a list.
     *
     * @param client Ore client
     * @param project Project to describe
     * @return List item
     * @throws IOException
     * @throws PluginNotInstalledException
     */
    public static Text getProjectListItem(OreClient client, Project project)
        throws IOException, PluginNotInstalledException {
        Text action;
        String pluginId = project.getPluginId();
        if (!client.isInstalled(pluginId))
            action = of(GREEN, BOLD, " Install");
        else if (client.isUpdateAvailable(pluginId))
            action = of(YELLOW, BOLD, " Update");
        else
            action = of(GRAY, BOLD, "Uninstall");
        return project.toText().concat(action);
    }

}
