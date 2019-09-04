package br.com.syrxcraft;

import br.com.syrxcraft.deps.Votifier;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class VoteParty extends JavaPlugin {
    static Logger logger = Logger.getLogger("VoteParty");
    static VoteParty instance;

    public static VoteParty getInstance() {
        return instance;
    }

    public static Logger Logger(){
        return logger;
    }

    @Override
    public void onEnable() {
        instance = this;

        /*
        Votifier Hook
         */
        new Votifier();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
