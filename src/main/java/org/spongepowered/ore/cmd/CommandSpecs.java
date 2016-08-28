package org.spongepowered.ore.cmd;

import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.remainingJoinedStrings;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.text.Text.of;
import static org.spongepowered.ore.text.Messages.DESCRIPTION_INSTALL;
import static org.spongepowered.ore.text.Messages.DESCRIPTION_SEARCH;
import static org.spongepowered.ore.text.Messages.DESCRIPTION_UNINSTALL;
import static org.spongepowered.ore.text.Messages.DESCRIPTION_UPDATE;
import static org.spongepowered.ore.text.Messages.DESCRIPTION_VERSION;

import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Collection of {@link CommandSpec}s used by Ore.
 */
public class CommandSpecs {

    private final CommandSpec install;
    private final CommandSpec uninstall;
    private final CommandSpec update;
    private final CommandSpec search;
    private final CommandSpec root;

    public CommandSpecs(CommandExecutors cmds) {
        this.install = CommandSpec.builder()
            .permission("ore.install")
            .description(DESCRIPTION_INSTALL.apply().build())
            .arguments(
                onlyOne(string(of("pluginId"))),
                optional(onlyOne(string(of("version"))))
            )
            .executor(cmds::installPlugin)
            .build();

        this.uninstall = CommandSpec.builder()
            .permission("ore.uninstall")
            .description(DESCRIPTION_UNINSTALL.apply().build())
            .arguments(onlyOne(string(of("pluginId"))))
            .executor(cmds::uninstallPlugin)
            .build();

        this.update = CommandSpec.builder()
            .permission("ore.update")
            .description(DESCRIPTION_UPDATE.apply().build())
            .arguments(
                onlyOne(string(of("pluginId"))),
                optional(onlyOne(string(of("version"))))
            )
            .executor(cmds::updatePlugin)
            .build();

        this.search = CommandSpec.builder()
            .permission("ore.search")
            .description(DESCRIPTION_SEARCH.apply().build())
            .arguments(remainingJoinedStrings(of("query")))
            .executor(cmds::searchForPlugins)
            .build();

        this.root = CommandSpec.builder()
            .permission("ore")
            .description(DESCRIPTION_VERSION.apply().build())
            .executor(cmds::showVersion)
            .child(this.install, "install", "get")
            .child(this.uninstall, "uninstall", "remove", "delete", "rm")
            .child(this.update, "update", "upgrade")
            .child(this.search, "search", "find")
            .build();
    }

    public CommandSpec getInstall() {
        return this.install;
    }

    public CommandSpec getUninstall() {
        return this.uninstall;
    }

    public CommandSpec getUpdate() {
        return this.update;
    }

    public CommandSpec getSearch() {
        return this.search;
    }

    public CommandSpec getRoot() {
        return this.root;
    }

}
