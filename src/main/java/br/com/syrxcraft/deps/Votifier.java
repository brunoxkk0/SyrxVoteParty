package br.com.syrxcraft.deps;

import br.com.syrxcraft.VoteParty;
import br.com.syrxcraft.core.events.VotifierEvt;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class Votifier {

    public Votifier(){
        Plugin votifier = Bukkit.getPluginManager().getPlugin("Votifier");
        Logger logger = VoteParty.Logger();

        if(votifier != null){
            String version = (votifier.getDescription() != null && votifier.getDescription().getVersion() != null) ? votifier.getDescription().getVersion() : "?";

            logger.info("Found Votifier... Hooking... [Votifier - " + version + "]");
            try{
                Bukkit.getPluginManager().registerEvents(new VotifierEvt(), VoteParty.getInstance());
                logger.info("Success!");
            }catch (Exception ignored){
                logger.info("Fail! Disabling...");
                VoteParty.getInstance().getPluginLoader().disablePlugin(VoteParty.getInstance());
            }

        }else {
            logger.info("This plugin require Votifier to work... disabling...");
            VoteParty.getInstance().getPluginLoader().disablePlugin(VoteParty.getInstance());
        }
    }

}
