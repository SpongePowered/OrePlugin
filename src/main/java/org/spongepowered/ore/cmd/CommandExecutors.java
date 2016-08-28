package org.spongepowered.ore.cmd;

import static org.spongepowered.api.text.Text.of;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.ore.client.SpongeOreClient.VERSION_RECOMMENDED;
import static org.spongepowered.ore.text.Messages.DOWNLOAD_RESTART_SERVER;
import static org.spongepowered.ore.text.Messages.REMOVAL;
import static org.spongepowered.ore.text.TextUtils.tuplePid;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.ore.OrePlugin;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.text.TextUtils;

import java.util.stream.Collectors;

/**
 * Ore command executors.
 */
public final class CommandExecutors {

    private static final String TASK_NAME_DOWNLOAD = "Ore Download";
    private static final String TASK_NAME_SEARCH = "Ore Search";

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
        newAsyncTask(TASK_NAME_DOWNLOAD, src, () -> {
            this.client.installPlugin(pluginId, version);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of("pluginId", of(pluginId), "phase",
                of("installation"))).build());
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
        newAsyncTask(TASK_NAME_DOWNLOAD, src, () -> {
            this.client.downloadUpdate(pluginId, version);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of("pluginId", of(pluginId), "phase",
                of("update"))).build());
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
        newAsyncTask(TASK_NAME_SEARCH, src, () -> {
            PaginationList.builder()
                .title(of(YELLOW, TASK_NAME_SEARCH))
                .contents(this.client.searchProjects(query).stream()
                    .<Text>map(project -> ((CommandTry<Text>) () ->
                        TextUtils.getProjectListItem(this.client, project)).callFor(src))
                    .collect(Collectors.toList()))
                .sendTo(src);
            return null;
        });
        return CommandResult.success();
    }

    private Task newAsyncTask(String name, CommandSource src, CommandTry callable) {
        return this.game.getScheduler().createTaskBuilder()
            .name(name)
            .async()
            .execute(() -> callable.callFor(src))
            .submit(this.plugin);
    }

}
