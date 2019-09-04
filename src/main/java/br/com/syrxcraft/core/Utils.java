package br.com.syrxcraft.core;

import br.com.syrxcraft.VoteParty;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Utils {

    public static boolean isVip(String username){
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        Permission permission = VoteParty.getPermission();

        if(player.hasPlayedBefore()){
            return permission.has(VoteParty.getWorldName(), username, "voteparty.vip");
        }else{
            return false;
        }
    }

}
