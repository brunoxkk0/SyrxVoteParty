package br.com.syrxcraft.core.data;

import br.com.syrxcraft.VoteParty;
import br.com.syrxcraft.core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Data {
    private static int Votes = 0;

    public static boolean process(String username){
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        if(player.hasPlayedBefore()){

            if(Utils.isVip(username)){
                Votes += 2;
            }else{
                Votes++;
            }

            if(Votes >= ConfigHolder.max_votes){
                callReward();
                Votes = 0;
                return true;
            }


        }else{
            VoteParty.Logger().info("[VoteParty] Invalid vote detected, player: " + username + " has never played on this server.");
            return false;
        }

        return false;
    }

    private static void callReward(){

    }


}
