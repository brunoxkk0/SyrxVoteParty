package br.com.syrxcraft.deps;

import br.com.syrxcraft.VoteParty;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

import static org.bukkit.Bukkit.getServer;

public class Vault {

    private Permission perms = null;

    public Vault(){
        Logger logger = VoteParty.Logger();
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");

        if(vault != null){
            String version = (vault.getDescription() != null && vault.getDescription().getVersion() != null) ? vault.getDescription().getVersion() : "?";
            logger.info("Found Vault... Hooking... [Vault - " + version + "]");

            try{
                RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
                perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();

                if(perms != null){
                    logger.info("Success!");
                }else{
                    logger.info("Fail! Disabling...");
                    VoteParty.getInstance().getPluginLoader().disablePlugin(VoteParty.getInstance());
                }

            }catch (Exception ignored){
                logger.info("Fail! Disabling...");
                VoteParty.getInstance().getPluginLoader().disablePlugin(VoteParty.getInstance());
            }

        }else{
            logger.info("This plugin require Vault to work... disabling...");
            VoteParty.getInstance().getPluginLoader().disablePlugin(VoteParty.getInstance());
        }

    }

    public Permission getPermisson() {
        return perms;
    }

}
