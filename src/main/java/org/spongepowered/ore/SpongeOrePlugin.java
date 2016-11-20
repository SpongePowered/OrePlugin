package org.spongepowered.ore;

import static org.spongepowered.api.text.Text.Builder;
import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.Text.of;
import static org.spongepowered.ore.Messages.AVAILABLE_UPDATES;
import static org.spongepowered.ore.Messages.UPDATE;

import com.google.common.collect.ImmutableMap;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.client.SpongeOreClient;
import org.spongepowered.ore.cmd.CommandExecutors;
import org.spongepowered.ore.cmd.CommandTry;
import org.spongepowered.ore.config.OreConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import javax.inject.Inject;

/**
 * Main plugin class for Ore.
 */
@Plugin(id = "ore",
        name = "Ore",
        description = "Official package manager for Sponge.",
        authors = { "windy" }
)
public final class SpongeOrePlugin implements OrePlugin {

    @Inject public Logger log;
    @Inject public Game game;
    @Inject public PluginContainer self;
    @Inject @DefaultConfig(sharedRoot = true) private Path configPath;

    private OreClient client;
    private OreConfig config;
    private CommandExecutors commands;

    @Listener(order = Order.POST)
    public void onStart(GameStartedServerEvent event) {
        this.log.info("Initializing...");
        if (!init())
            return;
        checkForUpdates();
        this.log.info("Done.");
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        this.log.info("Reloading...");
        this.commands.deregister();
        this.commands = null;
        this.client = null;
        this.config = null;
        if (!init())
            return;
        this.log.info("Done.");
    }

    private boolean init() {
        if (!loadConfig())
            return false;
        this.client = SpongeOreClient.forPlugin(this);
        if (this.client == null) {
            this.log.error("Ore failed to initialize the client. Commands are unavailable.");
            return false;
        }
        this.commands = new CommandExecutors(this).register();
        return true;
    }

    @Listener(order = Order.POST)
    public void onStop(GameStoppingEvent event) {
        if (this.client.hasUninstalledUpdates()) {
            this.log.info("Applying " + this.client.getUninstalledUpdates() + " updates...");
            try {
                this.client.installUpdates();
                this.log.info("Done.");
            } catch (IOException e) {
                this.log.error("An error occurred while applying downloaded updates.", e);
            }
        }

        if (this.client.hasPendingUninstallations()) {
            this.log.info("Uninstalling " + this.client.getPendingUninstallations() + " plugins...");
            try {
                this.client.completeUninstallations();
                this.log.info("Done.");
            } catch (IOException e) {
                this.log.error("An error occurred while completing pending uninstallations.", e);
            }
        }
    }

    @Override
    public OreClient getClient() {
        return this.client;
    }

    @Override
    public boolean loadConfig() {
        try {
            this.config = new OreConfig().load(this.configPath, this.self.getAsset("default.conf").get());
            return true;
        } catch (IOException e) {
            this.log.error("Failed to load configuration. Commands are unavailable.", e);
            return false;
        }
    }

    @Override
    public ConfigurationNode getConfigRoot() {
        return this.config.getRoot();
    }

    /**
     * Creates and executes a new async task for a command executor.
     *
     * @param name Task name
     * @param src CommandSource
     * @param callable To execute
     * @return Submitted task
     */
    public Task newAsyncTask(String name, CommandSource src, CommandTry callable) {
        return this.game.getScheduler().createTaskBuilder()
            .name(name)
            .async()
            .execute(() -> callable.callFor(src))
            .submit(this);
    }

    private void checkForUpdates() {
        this.log.info("Checking for updates...");
        ConsoleSource console = this.game.getServer().getConsole();
        newAsyncTask(CommandExecutors.TASK_NAME_SEARCH, console, () -> {
            Map<PluginContainer, String> updates = this.client.getAvailableUpdates();
            Builder message = AVAILABLE_UPDATES.apply(ImmutableMap.of("content", of(updates.size())));
            for (PluginContainer update : updates.keySet()) {
                message.append(NEW_LINE).append(UPDATE.apply(ImmutableMap.of(
                    "pluginId", of(update.getId()),
                    "content", of(updates.get(update))
                )).build());
            }
            console.sendMessage(message.build());
            return null;
        });
    }

}
