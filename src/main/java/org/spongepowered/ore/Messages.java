package org.spongepowered.ore;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.RED;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.api.text.format.TextStyles.BOLD;
import static org.spongepowered.api.text.format.TextStyles.ITALIC;
import static org.spongepowered.api.text.format.TextStyles.NONE;

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
    private static final TextTemplate.Arg CONTENT = arg("content").color(GRAY).style(NONE).build();

    // Command descriptions
    public static final Text DESCRIPTION_INSTALL = Text.of("Installs a new plugin.");
    public static final Text DESCRIPTION_UNINSTALL = Text.of("Uninstalls a plugin.");
    public static final Text DESCRIPTION_UPDATE = Text.of("Updates an installed plugin.");
    public static final Text DESCRIPTION_VERSION = Text.of("Displays versioning information about this plugin.");
    public static final Text DESCRIPTION_SEARCH = Text.of("Search for plugins on Ore.");
    public static final Text DESCRIPTION_SHOW = Text.of("Display information about a plugin.");

    // Messages
    public static final TextTemplate INSTALLING = of(YELLOW, "Installing plugin ", PLUGIN_ID, "...");
    public static final TextTemplate UPDATING = of(YELLOW, "Updating plugin ", PLUGIN_ID, "...");
    public static final TextTemplate SEARCHING = of(YELLOW, "Searching...");

    public static final TextTemplate NAME = of(YELLOW, BOLD, "Name: ", CONTENT);
    public static final TextTemplate ID = of(YELLOW, BOLD, "ID: ", CONTENT);
    public static final TextTemplate AUTHOR = of(YELLOW, BOLD, "Author: ", CONTENT);
    public static final TextTemplate CATEGORY = of(YELLOW, BOLD, "Category: ", CONTENT);
    public static final TextTemplate INSTALLED_VERSION = of(YELLOW, BOLD, "Installed: ", CONTENT);
    public static final TextTemplate RECOMMENDED_VERSION = of(YELLOW, BOLD, "Recommended: ", CONTENT);
    public static final TextTemplate LOADED = of(YELLOW, BOLD, "Loaded: ", CONTENT);
    public static final TextTemplate LOCATION = of(YELLOW, BOLD, "Location: ", CONTENT);

    public static final Text NOT_INSTALLED = Text.of(RED, "Not installed");
    public static final Text YES = Text.of("Yes");
    public static final Text NO_NEEDS_RESTART = Text.of("No (needs restart)");

    public static final TextTemplate VERSION = of(
        YELLOW, "Currently running ", arg("name").color(GREEN), " v", arg("version").color(GREEN));

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
