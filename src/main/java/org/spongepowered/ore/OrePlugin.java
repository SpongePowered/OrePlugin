package org.spongepowered.ore;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.ore.client.OreClient;

import javax.inject.Inject;

@Plugin(id = "ore",
        name = "OrePlugin",
        version = "1.0.0",
        description = "Official package manager for Sponge.",
        authors = { "windy" }
)
public final class OrePlugin {

    @Inject public Logger log;
    @Inject public Game game;

    private final OreClient client = new OreClient("http://localhost:9000");

    @Listener
    public void onStart(GameStartedServerEvent event) {
        this.log.info("Initializing...");
        new Commands(this).register();
        this.log.info("Done.");
    }

    public OreClient getClient() {
        return this.client;
    }

}
