package org.spongepowered.ore;

import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.RED;
import static org.spongepowered.api.text.format.TextStyles.ITALIC;

import org.spongepowered.api.text.TextTemplate;

/**
 * Collection of messages used by the plugin.
 */
public final class Messages {

    public static final TextTemplate DESCRIPTION_INSTALL = of("Installs a new plugin.");
    public static final TextTemplate DESCRIPTION_UNINSTALL = of("Uninstalls a plugin.");
    public static final TextTemplate DESCRIPTION_UPDATE = of("Updates an installed plugin.");
    public static final TextTemplate DESCRIPTION_VERSION = of("Displays versioning information about this plugin.");
    public static final TextTemplate DESCRIPTION_SEARCH = of("Search for plugins on Ore.");

    public static final TextTemplate ERROR_GENERAL = of(RED, "An error has occurred.");

    private static final TextTemplate.Arg PLUGIN_ID = arg("pluginId").color(GRAY).style(ITALIC).build();

    public static final TextTemplate ALREADY_INSTALLED = of(
        RED, "Plugin ", PLUGIN_ID, " is already installed.");

    public static final TextTemplate NOT_INSTALLED = of(
        RED, "Plugin ", PLUGIN_ID, " is not installed.");

    public static final TextTemplate DOWNLOAD_RESTART_SERVER = of(
        GREEN, "Download of update for ", PLUGIN_ID, " complete. Restart the server to complete ", arg("phase"), '.');

    public static final TextTemplate REMOVAL = of(
        GREEN, "Uninstalled plugin ", PLUGIN_ID, ". Restart the server to complete removal.");

    private Messages() {}

}
