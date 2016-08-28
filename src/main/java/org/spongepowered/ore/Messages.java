package org.spongepowered.ore;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.api.text.format.TextStyles.ITALIC;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.TextTemplate;

import java.util.Map;

/**
 * Collection of messages used by the plugin.
 */
public final class Messages {

    private static final TextTemplate.Arg PLUGIN_ID = arg("pluginId").color(GRAY).style(ITALIC).build();

    // Command descriptions
    public static final TextTemplate DESCRIPTION_INSTALL = of("Installs a new plugin.");
    public static final TextTemplate DESCRIPTION_UNINSTALL = of("Uninstalls a plugin.");
    public static final TextTemplate DESCRIPTION_UPDATE = of("Updates an installed plugin.");
    public static final TextTemplate DESCRIPTION_VERSION = of("Displays versioning information about this plugin.");
    public static final TextTemplate DESCRIPTION_SEARCH = of("Search for plugins on Ore.");

    // Messages
    public static final TextTemplate INSTALLING = of(YELLOW, "Installing plugin ", PLUGIN_ID, "...");
    public static final TextTemplate UPDATING = of(YELLOW, "Updating plugin ", PLUGIN_ID, "...");
    public static final TextTemplate SEARCHING = of(YELLOW, "Searching...");

    public static final TextTemplate DOWNLOAD_RESTART_SERVER = of(
        GREEN, "Download for ", PLUGIN_ID, " complete.",
        of(NEW_LINE, YELLOW, "Restart the server to complete "), arg("phase"), '.');

    public static final TextTemplate REMOVAL = of(
        GREEN, "Uninstalled plugin ", PLUGIN_ID, ".",
        of(NEW_LINE, YELLOW, "Restart the server to complete removal."));

    private Messages() {}

    /**
     * Maps the specified pluginId to the key "pluginId".
     *
     * @param pluginId Plugin ID
     * @return Map
     */
    public static Map<String, TextElement> tuplePid(String pluginId) {
        return ImmutableMap.of("pluginId", Text.of(pluginId));
    }

}
