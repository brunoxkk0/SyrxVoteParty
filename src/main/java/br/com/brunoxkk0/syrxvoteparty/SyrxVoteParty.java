package br.com.brunoxkk0.syrxvoteparty;

import br.com.brunoxkk0.syrxvoteparty.core.BarHandlerThread;
import br.com.brunoxkk0.syrxvoteparty.core.VoteHandler;
import com.google.inject.Inject;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import org.simpleyaml.configuration.Configuration;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Plugin(
        id = "syrxvoteparty",
        name = "SyrxVoteParty",
        description = "Reward your players when they reach the voting goal.",
        authors = {"brunoxkk0"},
        dependencies = {@Dependency(id = "nuvotifier", version = "2.5.3")}
)
public class SyrxVoteParty {

    private static PluginContainer container;
    private static SyrxVoteParty instance;
    private static YamlFile config;
    private static VoteHandler handler;
    private static BarHandlerThread barHandlerThread;

    @Inject
    private Logger logger = LoggerFactory.getLogger(SyrxVoteParty.class);

    @Listener
    public void onServerStart(GameStartingServerEvent event) throws IOException {
        container = Sponge.getPluginManager().fromInstance(this).get();


        File configFile = new File("config/" + container.getName()+"/config.yml");

        if(!configFile.getParentFile().exists()){
            if(configFile.getParentFile().mkdirs()){
                logger.info("[SyrxVoteParty] Criado pasta de configurações com sucesso!");
            }
        }

        if(!configFile.exists()){
            if(configFile.createNewFile()){
                logger.info("[SyrxVoteParty] Criado arquívo de configurações com sucesso!");
                config = loadConfig(configFile, true);
                return;
            }
        }

        logger.info("[SyrxVoteParty] Carregando configurações...");
        config = loadConfig(configFile, false);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;

        logger.info("[SyrxVoteParty] Carregando VoteHandler.");
        handler = new VoteHandler();
        VoteHandler.setup();

        logger.info("[SyrxVoteParty] Carregando BarHandlerThread.");
        barHandlerThread = new BarHandlerThread();
        BarHandlerThread.setup();

        logger.info("[SyrxVoteParty] Carregando eventos.");
        Sponge.getEventManager().registerListeners(getContainer(), handler);

    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event){
        logger.info("[SyrxVoteParty] Desligando sistemas e salvando dados...");
        VoteHandler.save();
        if(!BarHandlerThread.getWarn().isInterrupted()) BarHandlerThread.getWarn().interrupt();
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
        return config;
    }

    public YamlFile loadConfig(File file, boolean newConfig){
        config = new YamlFile(file);

        try {
            config.load();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            return null;
        }

        if(newConfig){

            config.set("votes_current",0);
            config.set("votes_required",50);
            config.set("bar_format","&3➜ &lVoteParty&r &7Restam: &3%c&r");
            config.set("reward_message","&8&l[&3&l&oSyrx&b&l&oCraft&8&l] &r&f&l➜ Meta de votos atingida...");
            config.set("reward_commands", Collections.singletonList("give %p diamond"));
            config.set("bar_warn_timer",10);
            config.set("bar_color","WHITE");
            config.set("bar_style","FLAT");
            config.set("counter_type","INVERTED");

            try {
                config.saveWithComments();
            } catch (IOException e) {
                return null;
            }
        }

        return config;
    }

    public static BarHandlerThread getBarHandlerThread() {
        return barHandlerThread;
    }

    public static VoteHandler getHandler() {
        return handler;
    }
}
