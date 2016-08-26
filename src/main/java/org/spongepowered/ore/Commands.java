package org.spongepowered.ore;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Commands {

    private final Ore plugin;
    private final Path downloadsPath = Paths.get("./updates");

    private final CommandSpec install = CommandSpec.builder()
            .permission("ore.install")
            .description(Text.of("Installs a new plugin."))
            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("pluginId"))))
            .executor(this::installPlugin)
            .build();

    private final CommandSpec root = CommandSpec.builder()
            .permission("ore")
            .description(Text.of("Displays versioning information about this plugin."))
            .executor(this::displayVersion)
            .child(this.install, "install", "get")
            .build();

    public Commands(Ore plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Sponge.getCommandManager().register(this.plugin, this.root, "ore");
    }

    private CommandResult displayVersion(CommandSource src, CommandContext context) {
        return CommandResult.success();
    }

    private CommandResult installPlugin(CommandSource src, CommandContext context) {
        String pluginId = context.<String>getOne("pluginId").get();
        Sponge.getScheduler().createTaskBuilder()
                .name("Ore Download")
                .async()
                .execute(() -> {
                    this.plugin.getApi().downloadPlugin(pluginId, this.downloadsPath);
                    src.sendMessage(Text.of("Download of " + pluginId + " complete."));
                })
                .submit(this.plugin);
        return CommandResult.success();
    }

}
