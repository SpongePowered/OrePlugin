package org.spongepowered.ore;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.client.SpongeOreClient;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

@Plugin(id = "ore",
        name = "OrePlugin",
        version = "1.0.0",
        description = "Official package manager for Sponge.",
        authors = { "windy" }
)
public final class OrePlugin {

    private static final String ROOT_URL = "http://localhost:9000";
    private static final Path MODS_DIR = Paths.get("./mods");
    private static final Path UPDATES_DIR = Paths.get("./updates");

    @Inject public Logger log;
    @Inject public Game game;

    private OreClient client;

    @Listener
    public void onStart(GameStartedServerEvent event) {
        this.log.info("Initializing...");
        this.client = new SpongeOreClient(ROOT_URL, MODS_DIR, UPDATES_DIR, this.game.getPluginManager());
        new Commands(this).register();
        this.log.info("Done.");
    }

    @Listener(order = Order.POST)
    public void onStop(GameStoppingEvent event) {
        if (this.client.hasUpdates()) {
            this.log.info("Applying " + this.client.updateCount() + " updates...");
            this.client.applyUpdates();
            this.log.info("Done.");
        }
    }

    public OreClient getClient() {
        return this.client;
    }

}
