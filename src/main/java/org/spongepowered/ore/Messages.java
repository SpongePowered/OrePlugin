package org.spongepowered.ore;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;
import static org.spongepowered.api.text.format.TextColors.*;
import static org.spongepowered.api.text.format.TextStyles.*;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Map;

/**
 * Collection of messages used by the plugin.
 */
public final class Messages {

    private static final TextTemplate.Arg PLUGIN_ID = arg("pluginId").color(GRAY).style(ITALIC).build();
    private static final TextTemplate.Arg CONTENT = arg("content").color(GRAY).style(TextStyles.NONE).build();

    // Command descriptions
    public static final Text DESCRIPTION_INSTALL = Text.of("Installs a new plugin.");
    public static final Text DESCRIPTION_DOWNLOAD = Text.of("Downloads a plugin to the downloads directory.");
    public static final Text DESCRIPTION_UNINSTALL = Text.of("Uninstalls a plugin.");
    public static final Text DESCRIPTION_UPDATE = Text.of("Updates an installed plugin.");
    public static final Text DESCRIPTION_RELOAD = Text.of("Reloads the plugin's configuration file.");
    public static final Text DESCRIPTION_VERSION = Text.of("Displays versioning information about this plugin.");
    public static final Text DESCRIPTION_SEARCH = Text.of("Search for plugins on Ore.");
    public static final Text DESCRIPTION_WHOIS = Text.of("Lists projects by the specified author.");
    public static final Text DESCRIPTION_SHOW = Text.of("Display information about a plugin.");
    public static final Text DESCRIPTION_DESCRIBE = Text.of("Displays the description of the plugin.");
    public static final Text DESCRIPTION_CONFIRM = Text.of("Confirms some pending action.");

    // Messages
    public static final TextTemplate INSTALLING = of(YELLOW, "Installing plugin ", PLUGIN_ID, "...");
    public static final TextTemplate DOWNLOADING = of(YELLOW, "Downloading plugin ", PLUGIN_ID, "...");
    public static final TextTemplate UPDATING = of(YELLOW, "Updating plugin ", PLUGIN_ID, "...");
    public static final TextTemplate FINDING = of(YELLOW, "Finding ", PLUGIN_ID, "...");
    public static final TextTemplate PLUGIN_NOT_FOUND = of(RED, "Plugin ", PLUGIN_ID, " not found.");
    public static final TextTemplate DESCRIPTION = of(arg("description").color(YELLOW));
    public static final TextTemplate CLIENT_MESSAGE = of(DARK_GREEN, "> ", arg("message"));

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
    public static final Text SEARCHING = Text.of(YELLOW, "Searching...");
    public static final Text DOWNLOAD_COMPLETE = Text.of(GREEN, "Download complete.");
    public static final Text RELOAD_COMPLETE = Text.of(GREEN, "Reload complete.");

    public static final TextTemplate AVAILABLE_UPDATES = of(
        YELLOW, "There are ", CONTENT, " updates available for download.");

    public static final TextTemplate UPDATE = of(arg("pluginId").color(YELLOW).style(BOLD), ": ", CONTENT);

    public static final TextTemplate VERSION = of(
        YELLOW, "Currently running ", arg("name").color(GREEN), " v", arg("version").color(GREEN));

    public static final TextTemplate DOWNLOAD_RESTART_SERVER = of(
        GREEN, "Download for ", PLUGIN_ID, " complete.",
        of(NEW_LINE, YELLOW, "Restart the server to complete "), arg("phase"), '.');

    public static final TextTemplate REMOVAL = of(
        GREEN, "Uninstalled plugin ", PLUGIN_ID, ".",
        of(NEW_LINE, YELLOW, "Restart the server to complete removal."));

    public static final TextTemplate USER_NOT_FOUND = of(
        RED, "User ", arg("username").color(GRAY).style(ITALIC), " not found.");

    public static final TextTemplate UNSUPPORTED_PLATFORM_VERSION = of(
        YELLOW, "This plugin requires a different major version of Sponge. ", NEW_LINE,
        "The plugin may not work as expected if you choose to continue.", NEW_LINE,
        "Required: ", arg("required").color(GREEN), NEW_LINE,
        "Current: ", arg("current").color(RED), NEW_LINE,
        "Install anyways?"
    );

    public static final Text CONFIRM_NONE = Text.of("Nothing to confirm!");
    public static final Text CONFIRM = Text.of(
        Text.builder("Yes").style(BOLD, UNDERLINE).color(GREEN).onClick(TextActions.runCommand("/ore confirm yes")),
        " ",
        Text.builder("No").style(BOLD, UNDERLINE).color(RED).onClick(TextActions.runCommand("/ore confirm no"))
    );

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

    public static PaginationList.Builder listBuilder(Text title) {
        return PaginationList.builder()
            .title(title)
            .padding(Text.of(BLUE, '-'));
    }

}
