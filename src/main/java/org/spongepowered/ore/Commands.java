package org.spongepowered.ore;

import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.ore.client.SpongeOreClient.VERSION_RECOMMENDED;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;

import java.util.Map;

/**
 * Ore command executors.
 */
public final class Commands {

    private final OrePlugin plugin;
    private final PluginManager pluginManager;
    private final Scheduler scheduler;

    private final CommandSpec install = CommandSpec.builder()
        .permission("ore.install")
        .description(Messages.DESCRIPTION_INSTALL.apply().build())
        .arguments(
            onlyOne(string(Text.of("pluginId"))),
            optional(onlyOne(string(Text.of("version"))))
        )
        .executor(this::installPlugin)
        .build();

    private final CommandSpec uninstall = CommandSpec.builder()
        .permission("ore.uninstall")
        .description(Messages.DESCRIPTION_UNINSTALL.apply().build())
        .arguments(onlyOne(string(Text.of("pluginId"))))
        .executor(this::uninstallPlugin)
        .build();

    private final CommandSpec update = CommandSpec.builder()
        .permission("ore.update")
        .description(Messages.DESCRIPTION_UPDATE.apply().build())
        .arguments(
            onlyOne(string(Text.of("pluginId"))),
            optional(onlyOne(string(Text.of("version"))))
        )
        .executor(this::updatePlugin)
        .build();

    private final CommandSpec root = CommandSpec.builder()
        .permission("ore")
        .description(Messages.DESCRIPTION_VERSION.apply().build())
        .executor(this::showVersion)
        .child(this.install, "install", "get")
        .child(this.uninstall, "uninstall", "remove", "delete", "rm")
        .child(this.update, "update", "upgrade")
        .build();

    public Commands(OrePlugin plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.game.getPluginManager();
        this.scheduler = plugin.game.getScheduler();
    }

    /**
     * Registers the executors with Sponge.
     */
    public void register() {
        this.plugin.game.getCommandManager().register(this.plugin, this.root, "ore");
    }

    private CommandResult showVersion(CommandSource src, CommandContext context) {
        return CommandResult.success();
    }

    private CommandResult installPlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (this.pluginManager.isLoaded(pluginId))
            throw new CommandException(Messages.ALREADY_INSTALLED.apply(tuplePid(pluginId)).build());

        newDownloadTask(() -> {
            this.plugin.getClient().installPlugin(pluginId, version);
            src.sendMessage(Text.of("Download of " + pluginId + " complete. Restart the server to complete "
                + "installation."));
        });

        return CommandResult.success();
    }

    private CommandResult uninstallPlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        if (!this.pluginManager.isLoaded(pluginId))
            throw new CommandException(Messages.NOT_INSTALLED.apply(tuplePid(pluginId)).build());
        this.plugin.getClient().uninstallPlugin(pluginId);
        src.sendMessage(Messages.REMOVAL.apply(tuplePid(pluginId)).build());
        return CommandResult.success();
    }

    private CommandResult updatePlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (!this.pluginManager.isLoaded(pluginId))
            throw new CommandException(Messages.NOT_INSTALLED.apply(tuplePid(pluginId)).build());

        newDownloadTask(() -> {
            this.plugin.getClient().downloadUpdate(pluginId, version);
            src.sendMessage(Messages.DOWNLOAD_RESTART_SERVER
                .apply(ImmutableMap.of("pluginId", Text.of(pluginId), "phase", Text.of("update"))).build());
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

    private Map<String, TextElement> tuplePid(String pluginId) {
        return ImmutableMap.of("pluginId", Text.of(pluginId));
    }

}
