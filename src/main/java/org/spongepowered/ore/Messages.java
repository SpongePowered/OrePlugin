package org.spongepowered.ore;

import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;

import org.spongepowered.api.text.TextTemplate;

/**
 * Collection of messages used by the plugin.
 */
public final class Messages {

    public static final TextTemplate DESCRIPTION_INSTALL = of("Installs a new plugin.");
    public static final TextTemplate DESCRIPTION_UNINSTALL = of("Uninstalls a plugin.");
    public static final TextTemplate DESCRIPTION_UPDATE = of("Updates an installed plugin.");
    public static final TextTemplate DESCRIPTION_VERSION = of("Displays versioning information about this plugin.");

    public static final TextTemplate ALREADY_INSTALLED = of(
        "Plugin \"", arg("pluginId"), "\" is already installed.");
    public static final TextTemplate NOT_INSTALLED = of("Plugin \"", arg("pluginId"), "\" is not installed.");
    public static final TextTemplate DOWNLOAD_RESTART_SERVER = of(
        "Download of update for ", arg("pluginId"), " complete. "
            + "Restart the server to complete ", arg("phase"), '.');
    public static final TextTemplate REMOVAL = of("Uninstalled plugin \"", arg("pluginId"), "\". "
        + "Restart the server to complete uninstallation.");

    private Messages() {}

}
