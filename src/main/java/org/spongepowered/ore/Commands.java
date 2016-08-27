package org.spongepowered.ore;

import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.ore.client.SpongeOreClient.VERSION_RECOMMENDED;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Commands {

    private final OrePlugin plugin;
    private final Path installDir = Paths.get("./mods");
    private final PluginManager pluginManager;
    private final Scheduler scheduler;

    private final CommandSpec install = CommandSpec.builder()
        .permission("ore.install")
        .description(Text.of("Installs a new plugin."))
        .arguments(
            onlyOne(string(Text.of("pluginId"))),
            optional(onlyOne(string(Text.of("version"))))
        )
        .executor(this::installPlugin)
        .build();

    private final CommandSpec update = CommandSpec.builder()
        .permission("ore.update")
        .description(Text.of("Updates an installed plugin."))
        .arguments(
            onlyOne(string(Text.of("pluginId"))),
            optional(onlyOne(string(Text.of("version"))))
        )
        .executor(this::updatePlugin)
        .build();

    private final CommandSpec root = CommandSpec.builder()
        .permission("ore")
        .description(Text.of("Displays versioning information about this plugin."))
        .executor(this::showVersion)
        .child(this.install, "install", "get")
        .child(this.update, "update", "upgrade")
        .build();

    public Commands(OrePlugin plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.game.getPluginManager();
        this.scheduler = plugin.game.getScheduler();
    }

    public void register() {
        Sponge.getCommandManager().register(this.plugin, this.root, "ore");
    }

    private CommandResult showVersion(CommandSource src, CommandContext context) {
        return CommandResult.success();
    }

    private CommandResult installPlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (this.pluginManager.isLoaded(pluginId))
            throw new CommandException(Text.of("Plugin \"" + pluginId + "\" is already installed."));
        newDownloadTask(() -> {
            this.plugin.getClient().installPlugin(pluginId, version);
            src.sendMessage(Text.of("Download of " + pluginId + " complete. Restart the server to complete "
                + "installation."));
        });
        return CommandResult.success();
    }

    private CommandResult updatePlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (!this.pluginManager.isLoaded(pluginId))
            throw new CommandException(Text.of("Plugin \"" + pluginId + "\" is not installed."));
        newDownloadTask(() -> {
            this.plugin.getClient().updatePlugin(pluginId, version);
            src.sendMessage(Text.of("Download of update for " + pluginId + " complete. Restart the server to complete "
                + "update."));
        });
        return CommandResult.success();
    }

    private Task newDownloadTask(Runnable r) {
        return this.scheduler.createTaskBuilder()
            .name("Ore Download")
            .async()
            .execute(r)
            .submit(this.plugin);
    }

}
