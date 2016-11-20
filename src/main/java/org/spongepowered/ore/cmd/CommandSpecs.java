package org.spongepowered.ore.cmd;

import static org.spongepowered.api.command.args.GenericArguments.choices;
import static org.spongepowered.api.command.args.GenericArguments.flags;
import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.remainingJoinedStrings;
import static org.spongepowered.api.command.args.GenericArguments.seq;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.text.Text.of;
import static org.spongepowered.ore.Messages.*;
import static org.spongepowered.ore.Permissions.CMD_DESCRIBE;
import static org.spongepowered.ore.Permissions.CMD_DOWNLOAD;
import static org.spongepowered.ore.Permissions.CMD_INSTALL;
import static org.spongepowered.ore.Permissions.CMD_RELOAD;
import static org.spongepowered.ore.Permissions.CMD_SEARCH;
import static org.spongepowered.ore.Permissions.CMD_SHOW;
import static org.spongepowered.ore.Permissions.CMD_UNINSTALL;
import static org.spongepowered.ore.Permissions.CMD_UPDATE;
import static org.spongepowered.ore.Permissions.CMD_VERSION;
import static org.spongepowered.ore.Permissions.CMD_WHOIS;

import com.google.common.collect.Maps;
import org.spongepowered.api.command.spec.CommandSpec;

/**
 * Collection of {@link CommandSpec}s used by Ore.
 */
public final class CommandSpecs {

    public static final String FLAG_WITH_DEPENDENCIES = "-withDependencies";
    public static final String FLAG_NO_DEPENDENCIES = "-noDependencies";
    public static final String FLAG_CANCEL = "-cancel";
    public static final String FLAG_CATEGORIES = "-categories";
    public static final String FLAG_SORT = "-sort";
    public static final String FLAG_IGNORE_PLATFORM_VERSION = "-ignorePlatformVersion";

    private final CommandSpec install;
    private final CommandSpec download;
    private final CommandSpec uninstall;
    private final CommandSpec update;
    private final CommandSpec search;
    private final CommandSpec whois;
    private final CommandSpec show;
    private final CommandSpec describe;
    private final CommandSpec confirm;
    private final CommandSpec reload;
    private final CommandSpec version;
    private final CommandSpec root;

    public CommandSpecs(CommandExecutors cmds) {
        this.install = CommandSpec.builder()
            .permission(CMD_INSTALL)
            .description(DESCRIPTION_INSTALL)
            .arguments(flags()
                .flag(FLAG_WITH_DEPENDENCIES)
                .flag(FLAG_NO_DEPENDENCIES)
                .flag(FLAG_CANCEL)
                .flag(FLAG_IGNORE_PLATFORM_VERSION)
                .buildWith(
                    seq(
                        onlyOne(string(of("pluginId"))),
                        optional(onlyOne(string(of("version"))))
                    )
                )
            )
            .executor(cmds::installPlugin)
            .build();

        this.download = CommandSpec.builder()
            .permission(CMD_DOWNLOAD)
            .description(DESCRIPTION_DOWNLOAD)
            .arguments(onlyOne(string(of("pluginId"))), optional(onlyOne(string(of("version")))))
            .executor(cmds::downloadPlugin)
            .build();

        this.uninstall = CommandSpec.builder()
            .permission(CMD_UNINSTALL)
            .description(DESCRIPTION_UNINSTALL)
            .arguments(flags().flag(FLAG_CANCEL).buildWith(onlyOne(string(of("pluginId")))))
            .executor(cmds::uninstallPlugin)
            .build();

        this.update = CommandSpec.builder()
            .permission(CMD_UPDATE)
            .description(DESCRIPTION_UPDATE)
            .arguments(flags().flag(FLAG_CANCEL).buildWith(
                seq(onlyOne(string(of("pluginId"))), optional(onlyOne(string(of("version")))))
            ))
            .executor(cmds::updatePlugin)
            .build();

        this.search = CommandSpec.builder()
            .permission(CMD_SEARCH)
            .description(DESCRIPTION_SEARCH)
            .arguments(flags()
                .valueFlag(string(of(FLAG_CATEGORIES)), FLAG_CATEGORIES)
                .valueFlag(choices(of(FLAG_SORT), Maps.newHashMap()), FLAG_SORT)
                .buildWith(remainingJoinedStrings(of("query")))
            )
            .executor(cmds::searchForPlugins)
            .build();

        this.whois = CommandSpec.builder()
            .permission(CMD_WHOIS)
            .description(DESCRIPTION_WHOIS)
            .arguments(onlyOne(string(of("username"))))
            .executor(cmds::showUser)
            .build();

        this.show = CommandSpec.builder()
            .description(DESCRIPTION_SHOW)
            .permission(CMD_SHOW)
            .arguments(onlyOne(string(of("pluginId"))))
            .executor(cmds::showPlugin)
            .build();

        this.describe = CommandSpec.builder()
            .description(DESCRIPTION_DESCRIBE)
            .permission(CMD_DESCRIBE)
            .arguments(onlyOne(string(of("pluginId"))))
            .executor(cmds::describePlugin)
            .build();

        this.confirm = CommandSpec.builder()
            .description(DESCRIPTION_CONFIRM)
            .arguments(onlyOne(string(of("choice"))))
            .executor(cmds::confirm)
            .build();

        this.reload = CommandSpec.builder()
            .permission(CMD_RELOAD)
            .description(DESCRIPTION_RELOAD)
            .executor(cmds::reloadConfig)
            .build();

        this.version = CommandSpec.builder()
            .permission(CMD_VERSION)
            .description(DESCRIPTION_VERSION)
            .executor(cmds::showVersion)
            .build();

        this.root = CommandSpec.builder()
            .permission(CMD_VERSION)
            .description(DESCRIPTION_VERSION)
            .executor(cmds::showVersion)
            .child(this.install, "install")
            .child(this.download, "download", "dl", "get")
            .child(this.uninstall, "uninstall", "remove", "delete", "rm")
            .child(this.update, "update", "upgrade")
            .child(this.show, "show", "info")
            .child(this.describe, "describe", "description")
            .child(this.search, "search", "find")
            .child(this.whois, "whois", "user", "author")
            .child(this.confirm, "confirm")
            .child(this.reload, "reload", "refresh")
            .child(this.version, "version")
            .build();
    }

    public CommandSpec getInstallSpec() {
        return this.install;
    }

    public CommandSpec getDownloadSpec() {
        return this.download;
    }

    public CommandSpec getUninstallSpec() {
        return this.uninstall;
    }

    public CommandSpec getUpdateSpec() {
        return this.update;
    }

    public CommandSpec getShowSpec() {
        return this.show;
    }

    public CommandSpec getDescribeSpec() {
        return this.describe;
    }

    public CommandSpec getSearchSpec() {
        return this.search;
    }

    public CommandSpec getWhoisSpec() {
        return this.whois;
    }

    public CommandSpec getConfirmSpec() {
        return this.confirm;
    }

    public CommandSpec getReloadSpec() {
        return this.reload;
    }

    public CommandSpec getVersionSpec() {
        return this.version;
    }

    public CommandSpec getRootSpec() {
        return this.root;
    }

}
