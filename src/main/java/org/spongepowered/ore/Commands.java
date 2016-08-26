package org.spongepowered.ore;

import static org.spongepowered.ore.client.OreClient.VERSION_RECOMMENDED;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Commands {

    private final OrePlugin plugin;
    private final Path installDir = Paths.get("./mods");

    private final CommandSpec install = CommandSpec.builder()
            .permission("ore.install")
            .description(Text.of("Installs a new plugin."))
            .arguments(
                GenericArguments.onlyOne(GenericArguments.string(Text.of("pluginId"))),
                GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("version"))))
            )
            .executor(this::installPlugin)
            .build();

    private final CommandSpec root = CommandSpec.builder()
            .permission("ore")
            .description(Text.of("Displays versioning information about this plugin."))
            .executor(this::displayVersion)
            .child(this.install, "install", "get")
            .build();

    public Commands(OrePlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Sponge.getCommandManager().register(this.plugin, this.root, "ore");
    }

    private CommandResult displayVersion(CommandSource src, CommandContext context) {
        return CommandResult.success();
    }

    private CommandResult installPlugin(CommandSource src, CommandContext context) throws CommandException {
        String pluginId = context.<String>getOne("pluginId").get();
        String version = context.<String>getOne("version").orElse(VERSION_RECOMMENDED);
        if (Sponge.getPluginManager().isLoaded(pluginId))
            throw new CommandException(Text.of("Plugin \"" + pluginId + "\" is already installed."));
        Sponge.getScheduler().createTaskBuilder()
                .name("Ore Download")
                .async()
                .execute(() -> {
                    this.plugin.getClient().installPlugin(pluginId, version, this.installDir);
                    src.sendMessage(Text.of("Download of " + pluginId + " complete. Restart the server to complete "
                        + "installation."));
                })
                .submit(this.plugin);
        return CommandResult.success();
    }

}
