package br.com.brunoxkk0.syrxvoteparty.core;

import br.com.brunoxkk0.syrxvoteparty.SyrxVoteParty;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VoteHandler {

    private static int currentVoteCount;
    private static int requiredVotesCount;
    private static List<String> rewardCommands;
    private static String rewardMessage;

    public void setup(){

        currentVoteCount =  SyrxVoteParty.getConfig().getInt("votes_current");
        requiredVotesCount = SyrxVoteParty.getConfig().getInt("votes_required");
        rewardCommands = SyrxVoteParty.getConfig().getStringList("reward_commands");
        rewardMessage = SyrxVoteParty.getConfig().getString("reward_message");

    }

    public static float getVotePercentage(CounterType type) {
        if (type == CounterType.INVERTED) {
            return Math.min(100F, Math.max((100 - (currentVoteCount * 100) / requiredVotesCount),0F));
        }
        return Math.min(100F, Math.max((currentVoteCount * 100 / requiredVotesCount),0F));
    }

    public synchronized void process(Player player){

        currentVoteCount++;

        if(currentVoteCount >= requiredVotesCount){
            currentVoteCount = 0;

            Sponge.getServer().getBroadcastChannel().send(Text.of(rewardMessage.replace("&","\u00a7")));
            for (String cmd : rewardCommands){
                rewardAll(cmd);
            }

        }

        if(player != null){
            SyrxVoteParty.getBarHandlerThread().processBar(player);
        }
    }


    public void rewardAll(String command){

        for(Player player : Sponge.getServer().getOnlinePlayers()){

            if(command.startsWith("/")){
                command  = command.substring(1);
            }

            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command.replace("%p", player.getName()));
            player.playSound(SoundTypes.ENTITY_PLAYER_LEVELUP,player.getPosition(),1);
        }

    }

    public void save(){
        SyrxVoteParty.getConfig().set("votes_current",currentVoteCount);

        try {
            SyrxVoteParty.getConfig().saveWithComments();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static int getCurrentVoteCount() {
        return currentVoteCount;
    }

    public static int getRequiredVotesCount() {
        return requiredVotesCount;
    }

    public static int getMissingVotesCount(){
        return (requiredVotesCount - currentVoteCount);
    }


    public synchronized void onVote(VotifierEvent event){

        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        if(!userStorage.get().get(event.getVote().getUsername()).isPresent()){
            SyrxVoteParty.getInstance().getLogger().info("[SyrxVoteParty] O jogador " + event.getVote().getUsername() + " nunca jogou no servidor antes.");
            return;
        }

        if(userStorage.get().get(event.getVote().getUsername()).get().getPlayer().isPresent()){
            process(userStorage.get().get(event.getVote().getUsername()).get().getPlayer().get());
            return;
        }

        SyrxVoteParty.getInstance().getLogger().info("[SyrxVoteParty] O jogador " + event.getVote().getUsername() + " votou no servidor. mais estava offline.");

    }

}
