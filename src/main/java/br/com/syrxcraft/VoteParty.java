package br.com.syrxcraft;

import br.com.syrxcraft.deps.Vault;
import br.com.syrxcraft.deps.Votifier;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Logger;

public class VoteParty extends JavaPlugin {

    private static Logger logger = Logger.getLogger("VoteParty");
    private static VoteParty instance;
    private static Permission permission = null;
    private static String worldName = "";

    public static VoteParty getInstance() {
        return instance;
    }

    public static Logger Logger(){
        return logger;
    }

    public static String getWorldName() {
        return worldName;
    }

    @Override
    public void onEnable() {
        instance = this;
        updateWorld();

        /*
        Votifier Hook
         */
        new Votifier();

        /*
        Vault Hook
         */
        Vault vault = new Vault();
        permission = vault.getPermisson();
    }

    public static Permission getPermission() {
        return permission;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void updateWorld() {
        File server_propreties = new File("server.properties");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(server_propreties));

            String line = reader.readLine();
            while (line != null) {

                if(line.startsWith("level-name")){
                    String[] lin = line.split("=");

                    worldName = lin[1];
                    break;
                }

                line = reader.readLine();
            }

            reader.close();

        } catch (IOException ignored) {
            Logger().warning("Fail to get a world name.... Disabling");
            this.getPluginLoader().disablePlugin(this);
        }
    }

}
