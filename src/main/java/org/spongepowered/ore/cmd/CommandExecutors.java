package org.spongepowered.ore.cmd;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.Text.of;
import static org.spongepowered.api.text.format.TextColors.BLUE;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.ore.Messages.*;
import static org.spongepowered.ore.Messages.tuplePid;
import static org.spongepowered.ore.client.SpongeOreClient.VERSION_RECOMMENDED;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.ore.OrePlugin;
import org.spongepowered.ore.client.Installation;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.client.model.Project;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Ore command executors.
 */
public final class CommandExecutors {

    public static final String TASK_NAME_DOWNLOAD = "Ore Download";
    public static final String TASK_NAME_SEARCH = "Ore Search";
    private final OrePlugin plugin;
    private final OreClient client;
    private final Game game;

    /**
     * Constructs a new instance of the executors, ready for registration.
     *
     * @param plugin Ore plugin
     */
    public CommandExecutors(OrePlugin plugin) {
        this.plugin = plugin;
        this.client = plugin.getClient();
        this.game = plugin.game;
    }

    /**
     * Registers the executors with Sponge.
     */
    public void register() {
        this.game.getCommandManager().register(this.plugin, new CommandSpecs(this).getRoot(), "ore");
    }

    /**
     * Displays versioning information about the plugin.
     *
     * @param src source of command
     * @param context CommandContext
     * @return result of command
     */
    public CommandResult showVersion(CommandSource src, CommandContext context) {
        src.sendMessage(VERSION.apply(ImmutableMap.of(
            "name", of(this.plugin.self.getName()),
            "version", of(this.plugin.self.getVersion())
        )).build());
        return CommandResult.success();
    }

    /**
     * Downloads and installs a plugin.
     *
     * @param src source of command
     * @param context CommandContext
     * @return result of command
     */
    public CommandResult installPlugin(CommandSource src, CommandContext context) {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        src.sendMessage(INSTALLING.apply(tuplePid(pluginId)).build());
        this.plugin.newAsyncTask(TASK_NAME_DOWNLOAD, src, () -> {
            this.client.installPlugin(pluginId, version);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of(
                "pluginId", of(pluginId),
                "phase", of("installation")
            )).build());
            return null;
        });
        return CommandResult.success();
    }

    /**
     * Uninstalls a plugin.
     *
     * @param src source of command
     * @param context CommandContext
     * @return result of command
     */
    public CommandResult uninstallPlugin(CommandSource src, CommandContext context) {
        String pluginId = context.<String>getOne("pluginId").get();
        ((CommandTry) () -> {
            this.client.uninstallPlugin(pluginId);
            return null;
        }).callFor(src);
        src.sendMessage(REMOVAL.apply(tuplePid(pluginId)).build());
        return CommandResult.success();
    }

    /**
     * Downloads and updates a plugin.
     *
     * @param src source of command
     * @param context CommandContext
     * @return result of command
     */
    public CommandResult updatePlugin(CommandSource src, CommandContext context) {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        src.sendMessage(UPDATING.apply(tuplePid(pluginId)).build());
        this.plugin.newAsyncTask(TASK_NAME_DOWNLOAD, src, () -> {
            this.client.downloadUpdate(pluginId, version);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of(
                "pluginId", of(pluginId),
                "phase", of("update")
            )).build());
            return null;
        });
        return CommandResult.success();
    }

    /**
     * Displays search results for a given project query.
     *
     * @param src source of command
     * @param context CommandContext
     * @return result of command
     */
    public CommandResult searchForPlugins(CommandSource src, CommandContext context) {
        String query = context.<String>getOne("query").get();
        src.sendMessage(SEARCHING);
        this.plugin.newAsyncTask(TASK_NAME_SEARCH, src, () -> {
            PaginationList.builder()
                .title(of(YELLOW, TASK_NAME_SEARCH))
                .padding(of(BLUE, '-'))
                .contents(this.client.searchProjects(query).stream()
                    .<Text>map(project -> ((CommandTry<Text>) () ->
                        ProjectListItem.of(this.plugin, project).toText()).callFor(src))
                    .collect(Collectors.toList()))
                .sendTo(src);
            return null;
        });
        return CommandResult.success();
    }

    /**
     * Displays details about a specified plugin.
     *
     * @param src CommandSource
     * @param context CommandContext
     * @return result of command
     */
    public CommandResult showPlugin(CommandSource src, CommandContext context) {
        String pluginId = context.<String>getOne("pluginId").get();
        src.sendMessage(FINDING.apply(tuplePid(pluginId)).build());
        this.plugin.newAsyncTask(TASK_NAME_SEARCH, src, () -> {
            Optional<Project> projectOpt = this.client.getProject(pluginId);
            if (projectOpt.isPresent()) {
                Project project = projectOpt.get();

                // Installed version
                Text version = NOT_INSTALLED;
                Optional<Installation> installOpt = this.client.getInstallation(pluginId);
                boolean installed = installOpt.isPresent();
                if (installed)
                    version = of(installOpt.get().getVersion());

                Text recommended = of(project.getRecommendedVersion().getName());
                Text.Builder message = NAME.apply(ImmutableMap.of("content", of(project.getName())))
                    .append(NEW_LINE)
                    .append(ID.apply(ImmutableMap.of("content", of(project.getPluginId()))).build())
                    .append(NEW_LINE)
                    .append(AUTHOR.apply(ImmutableMap.of("content", of(project.getOwnerName()))).build())
                    .append(NEW_LINE)
                    .append(CATEGORY.apply(ImmutableMap.of("content", of(project.getCategory().getTitle()))).build())
                    .append(NEW_LINE)
                    .append(INSTALLED_VERSION.apply(ImmutableMap.of("content", version)).build())
                    .append(NEW_LINE)
                    .append(RECOMMENDED_VERSION.apply(ImmutableMap.of("content", recommended)).build());

                if (installed) {
                    // Additional install information
                    Text answer = this.game.getPluginManager().isLoaded(pluginId) ? YES : NO_NEEDS_RESTART;
                    Text path = of(installOpt.get().getPath().toAbsolutePath().toString());
                    message.append(NEW_LINE)
                        .append(LOADED.apply(ImmutableMap.of("content", answer)).build())
                        .append(NEW_LINE)
                        .append(LOCATION.apply(ImmutableMap.of("content", path)).build());
                }

                src.sendMessage(message.build());
            } else
                src.sendMessage(NOT_FOUND.apply(tuplePid(pluginId)).build());
            return null;
        });
        return CommandResult.success();
    }

}
