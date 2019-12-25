package br.com.brunoxkk0.syrxvoteparty;

import br.com.brunoxkk0.scs.API.config.ConfigAPI;
import br.com.brunoxkk0.scs.API.config.ResouceContainer;
import br.com.brunoxkk0.syrxvoteparty.core.BarHandlerThread;
import br.com.brunoxkk0.syrxvoteparty.core.VoteHandler;
import com.google.inject.Inject;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import scslibs.org.simpleyaml.configuration.file.YamlFile;
import scslibs.org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;

@Plugin(
        id = "syrxvoteparty",
        name = "SyrxVoteParty",
        description = "Reward your players when they reach the voting goal.",
        authors = {"brunoxkk0"},
        dependencies = {@Dependency(id = "nuvotifier", version = "2.5.3"),@Dependency(id = "syrxcoresponge", version = "1.0")}
)
public class SyrxVoteParty {

    private static PluginContainer container;
    private static SyrxVoteParty instance;
    private static ConfigAPI config;
    private static VoteHandler handler;
    private static BarHandlerThread barHandlerThread;

    @Inject
    private Logger logger = LoggerFactory.getLogger("Syrx-VoteParty");

    @Listener
    public void onServerStart(GameStartingServerEvent event) throws IOException, InvalidConfigurationException {

        container = Sponge.getPluginManager().fromInstance(this).get();
        ResouceContainer resouceContainer =  new ResouceContainer(getClass().getResourceAsStream("/syrxvoteparty/config.yml"));
        config = new ConfigAPI(container, "config.yml",true,resouceContainer);

    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;
        logger.info("[SyrxVoteParty] Carregando eventos.");
        logger.info("[SyrxVoteParty] Carregando VoteHandler.");
        handler = new VoteHandler();
        handler.setup();

        logger.info("[SyrxVoteParty] Carregando BarHandlerThread.");
        barHandlerThread = new BarHandlerThread();
        barHandlerThread.setup();

    }

    @Listener
    public void onVote(VotifierEvent event){
        handler.onVote(event);
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event){
        logger.info("[SyrxVoteParty] Desligando sistemas e salvando dados...");
        handler.save();

        if(!barHandlerThread.getWarn().isInterrupted()) barHandlerThread.getWarn().interrupt();

    }

    public Logger getLogger() {
        return logger;
    }

    public static PluginContainer getContainer() {
        return container;
    }

    public static SyrxVoteParty getInstance() {
        return instance;
    }

    public static YamlFile getConfig() {
        return config.getConfig();
    }

    public static BarHandlerThread getBarHandlerThread() {
        return barHandlerThread;
    }

    public static VoteHandler getHandler() {
        return handler;
    }
}
