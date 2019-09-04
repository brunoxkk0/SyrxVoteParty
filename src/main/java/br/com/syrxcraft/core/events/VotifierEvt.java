package br.com.syrxcraft.core.events;

import br.com.syrxcraft.core.data.Data;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierEvt implements Listener {

    @EventHandler
    public void onVote(VotifierEvent event){
        Data.process(event.getVote().getUsername());
    }

}
