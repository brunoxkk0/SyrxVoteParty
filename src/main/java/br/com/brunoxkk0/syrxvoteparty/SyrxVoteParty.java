package br.com.brunoxkk0.syrxvoteparty;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "syrxvoteparty",
        name = "SyrxVoteParty",
        description = "Reward your players when they reach the voting goal.",
        authors = {"brunoxkk0"}
)
public class SyrxVoteParty {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
