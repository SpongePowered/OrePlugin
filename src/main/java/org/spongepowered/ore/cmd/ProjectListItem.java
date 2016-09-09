package org.spongepowered.ore.cmd;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.Text.builder;
import static org.spongepowered.api.text.action.TextActions.executeCallback;
import static org.spongepowered.api.text.action.TextActions.openUrl;
import static org.spongepowered.api.text.action.TextActions.showText;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.RED;
import static org.spongepowered.api.text.format.TextColors.RESET;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.api.text.format.TextStyles.BOLD;
import static org.spongepowered.api.text.format.TextStyles.ITALIC;
import static org.spongepowered.ore.Messages.DOWNLOAD_RESTART_SERVER;
import static org.spongepowered.ore.Messages.INSTALLING;
import static org.spongepowered.ore.Messages.REMOVAL;
import static org.spongepowered.ore.Messages.UPDATING;
import static org.spongepowered.ore.client.OreClient.VERSION_RECOMMENDED;
import static org.spongepowered.ore.cmd.CommandExecutors.TASK_NAME_DOWNLOAD;
import static org.spongepowered.ore.Messages.tuplePid;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;
import org.spongepowered.ore.OrePlugin;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.client.exception.PluginNotInstalledException;
import org.spongepowered.ore.client.model.Project;

import java.io.IOException;

/**
 * A single interactive {@link TextRepresentable} that describes a
 * {@link Project}.
 */
public final class ProjectListItem implements TextRepresentable {

    final OrePlugin plugin;
    final OreClient client;
    final Project project;
    final String pluginId;
    final Text text;

    ProjectListItem(OrePlugin plugin, Project project) throws IOException, PluginNotInstalledException {
        this.plugin = plugin;
        this.client = plugin.getClient();
        this.project = project;

        Text title = this.project.toText().toBuilder()
            .onClick(openUrl(project.getHomepageUrl(this.client)))
            .onHover(showText(builder()
                .append(Text.of(project.getName() + ": " + project.getDescription()))
                .append(NEW_LINE, builder().append(Text.of(GRAY, ITALIC, "Click to visit homepage")).build())
                .build()))
            .build();

        Text action;
        this.pluginId = this.project.getPluginId();
        OreClient client = this.plugin.getClient();
        if (!client.isInstalled(this.pluginId)) {
            action = builder()
                .append(Text.of(GREEN, BOLD, "Install"))
                .onClick(executeCallback(this::onInstallClick))
                .build();
        } else if (client.isUpdateAvailable(this.pluginId)) {
            action = builder()
                .append(Text.of(YELLOW, BOLD, "Update"))
                .onClick(executeCallback(this::onUpdateClick))
                .build();
        } else {
            action = builder()
                .append(Text.of(RED, BOLD, "Uninstall"))
                .onClick(executeCallback(this::onUninstallClick))
                .build();
        }

        this.text = title.concat(Text.of(RESET, " ")).concat(action);

    }

    /**
     * Returns the {@link Project} this item describes.
     *
     * @return Project item describes
     */
    public Project getProject() {
        return this.project;
    }

    private void onInstallClick(CommandSource src) {
        this.plugin.newAsyncTask(TASK_NAME_DOWNLOAD, src, () -> {
            src.sendMessage(INSTALLING.apply(tuplePid(this.pluginId)).build());
            this.client.installPlugin(this.pluginId, VERSION_RECOMMENDED);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of(
                "pluginId", Text.of(this.pluginId),
                "phase", Text.of("installation")
            )).build());
            return null;
        });
    }

    private void onUpdateClick(CommandSource src) {
        this.plugin.newAsyncTask(TASK_NAME_DOWNLOAD, src, () -> {
            src.sendMessage(UPDATING.apply(tuplePid(this.pluginId)).build());
            this.client.updatePlugin(this.pluginId, VERSION_RECOMMENDED);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of(
                "pluginId", Text.of(this.pluginId),
                "phase", Text.of("update")
            )).build());
            return null;
        });
    }

    private void onUninstallClick(CommandSource src) {
        ((CommandTry) () -> {
            this.client.uninstallPlugin(this.pluginId);
            return null;
        }).callFor(src);
        src.sendMessage(REMOVAL.apply(tuplePid(this.pluginId)).build());
    }

    @Override
    public Text toText() {
        return this.text;
    }

    /**
     * Creates a new {@link ProjectListItem} for the specified plugin and
     * {@link Project}.
     *
     * @param plugin Plugin
     * @param project Project
     * @return New item
     * @throws IOException
     * @throws PluginNotInstalledException
     */
    public static ProjectListItem of(OrePlugin plugin, Project project)
        throws IOException, PluginNotInstalledException {
        return new ProjectListItem(plugin, project);
    }

}
