package org.spongepowered.ore;

import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.remainingJoinedStrings;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.text.Text.*;
import static org.spongepowered.api.text.format.TextColors.YELLOW;
import static org.spongepowered.ore.Messages.ALREADY_INSTALLED;
import static org.spongepowered.ore.Messages.DESCRIPTION_INSTALL;
import static org.spongepowered.ore.Messages.DESCRIPTION_SEARCH;
import static org.spongepowered.ore.Messages.DESCRIPTION_UNINSTALL;
import static org.spongepowered.ore.Messages.DESCRIPTION_UPDATE;
import static org.spongepowered.ore.Messages.DESCRIPTION_VERSION;
import static org.spongepowered.ore.Messages.DOWNLOAD_RESTART_SERVER;
import static org.spongepowered.ore.Messages.ERROR_GENERAL;
import static org.spongepowered.ore.Messages.NOT_INSTALLED;
import static org.spongepowered.ore.Messages.REMOVAL;
import static org.spongepowered.ore.client.SpongeOreClient.VERSION_RECOMMENDED;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.model.Project;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ore command executors.
 */
public final class Commands {

    private static final String TASK_NAME_DOWNLOAD = "Ore Download";
    private static final String TASK_NAME_SEARCH = "Ore Search";

    private final OrePlugin plugin;
    private final OreClient client;
    private final Game game;
    private final PluginManager pluginManager;

    private final CommandSpec install = CommandSpec.builder()
        .permission("ore.install")
        .description(DESCRIPTION_INSTALL.apply().build())
        .arguments(
            onlyOne(string(of("pluginId"))),
            optional(onlyOne(string(of("version"))))
        )
        .executor(this::installPlugin)
        .build();

    private final CommandSpec uninstall = CommandSpec.builder()
        .permission("ore.uninstall")
        .description(DESCRIPTION_UNINSTALL.apply().build())
        .arguments(onlyOne(string(of("pluginId"))))
        .executor(this::uninstallPlugin)
        .build();

    private final CommandSpec update = CommandSpec.builder()
        .permission("ore.update")
        .description(DESCRIPTION_UPDATE.apply().build())
        .arguments(
            onlyOne(string(of("pluginId"))),
            optional(onlyOne(string(of("version"))))
        )
        .executor(this::updatePlugin)
        .build();

    private final CommandSpec search = CommandSpec.builder()
        .permission("ore.search")
        .description(DESCRIPTION_SEARCH.apply().build())
        .arguments(remainingJoinedStrings(of("query")))
        .executor(this::searchForPlugins)
        .build();

    private final CommandSpec root = CommandSpec.builder()
        .permission("ore")
        .description(DESCRIPTION_VERSION.apply().build())
        .executor(this::showVersion)
        .child(this.install, "install", "get")
        .child(this.uninstall, "uninstall", "remove", "delete", "rm")
        .child(this.update, "update", "upgrade")
        .child(this.search, "search", "find")
        .build();

    public Commands(OrePlugin plugin) {
        this.plugin = plugin;
        this.client = plugin.getClient();
        this.game = plugin.game;
        this.pluginManager = this.game.getPluginManager();
    }

    /**
     * Registers the executors with Sponge.
     */
    public void register() {
        this.game.getCommandManager().register(this.plugin, this.root, "ore");
    }

    private CommandResult showVersion(CommandSource src, CommandContext context) {
        return CommandResult.success();
    }

    private CommandResult installPlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (this.pluginManager.isLoaded(pluginId))
            throw new CommandException(ALREADY_INSTALLED.apply(tuplePid(pluginId)).build());

        newAsyncTask(TASK_NAME_DOWNLOAD, () -> {
            this.client.installPlugin(pluginId, version);
            src.sendMessage(of("Download of " + pluginId + " complete. Restart the server to complete "
                + "installation."));
        });

        return CommandResult.success();
    }

    private CommandResult uninstallPlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        if (!this.pluginManager.isLoaded(pluginId))
            throw new CommandException(NOT_INSTALLED.apply(tuplePid(pluginId)).build());
        this.client.uninstallPlugin(pluginId);
        src.sendMessage(REMOVAL.apply(tuplePid(pluginId)).build());
        return CommandResult.success();
    }

    private CommandResult updatePlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (!this.pluginManager.isLoaded(pluginId))
            throw new CommandException(NOT_INSTALLED.apply(tuplePid(pluginId)).build());

        newAsyncTask(TASK_NAME_DOWNLOAD, () -> {
            this.client.downloadUpdate(pluginId, version);
            src.sendMessage(DOWNLOAD_RESTART_SERVER.apply(ImmutableMap.of("pluginId", of(pluginId), "phase",
                of("update"))).build());
        });

        return CommandResult.success();
    }

    private CommandResult searchForPlugins(CommandSource src, CommandContext context) {
        String query = context.<String>getOne("query").get();
        newAsyncTask(TASK_NAME_SEARCH, () -> {
            List<Project> result = null;
            try {
                result = this.client.searchProjects(query);
            } catch (IOException e) {
                src.sendMessage(ERROR_GENERAL.apply().build());
                throw new RuntimeException(e);
            }

            PaginationList.builder()
                .title(of(YELLOW, TASK_NAME_SEARCH))
                .contents(result.stream()
                    .<Text>map(Project::toText)
                    .collect(Collectors.toList()))
                .sendTo(src);
        });
        return CommandResult.success();
    }

    private Task newAsyncTask(String name, Runnable r) {
        return this.game.getScheduler().createTaskBuilder()
            .name(name)
            .async()
            .execute(r)
            .submit(this.plugin);
    }

    private Map<String, TextElement> tuplePid(String pluginId) {
        return ImmutableMap.of("pluginId", of(pluginId));
    }

}
