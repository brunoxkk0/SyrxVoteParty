package br.com.brunoxkk0.syrxvoteparty.core;

import br.com.brunoxkk0.syrxvoteparty.SyrxVoteParty;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.*;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import static java.lang.Thread.sleep;

public class BarHandlerThread {

    private static Thread warn;
    private static String barMessage;
    private static BossBarColor color;
    private static BossBarOverlay overlay;
    private static CounterType counterType;
    private static int bar_warn_timer;

    public Thread getWarn() {
        return warn;
    }

    public void setup(){

        barMessage = SyrxVoteParty.getConfig().getString("bar_format");
        loadColor(SyrxVoteParty.getConfig().getString("bar_color"));
        loadStyle(SyrxVoteParty.getConfig().getString("bar_style"));
        counterType = CounterType.getByName(SyrxVoteParty.getConfig().getString("counter_type"));
        bar_warn_timer = SyrxVoteParty.getConfig().getInt("bar_warn_timer");


        warn = new Thread(() -> {
            ServerBossBar bar;
            while (!warn.isInterrupted()){
                bar = ServerBossBar.builder().name(Text.of(barMessage.replace("%m",""+VoteHandler.getMissingVotesCount()).replace("%c",""+VoteHandler.getCurrentVoteCount()).replace("%r",""+VoteHandler.getRequiredVotesCount()).replace("&","\u00a7"))).color(color).overlay(overlay).percent(VoteHandler.getVotePercentage(counterType)/100).build();
                bar.addPlayers(Sponge.getServer().getOnlinePlayers());
                bar.setVisible(true);
                try {
                    sleep(15000);
                } catch (InterruptedException ignored) { }
                bar.setVisible(false);
                bar.removePlayers(bar.getPlayers());
                try {
                    sleep(bar_warn_timer*60000);
                } catch (InterruptedException ignored) { }

                try {
                    SyrxVoteParty.getHandler().save();
                    SyrxVoteParty.getInstance().getLogger().info("[SyrxVoteParty] Votos salvos com sucesso.");
                }catch (Exception e){
                    SyrxVoteParty.getInstance().getLogger().info("[SyrxVoteParty] Falha ao salvar os votos.");
                }
            }
        });

        new Thread(warn).start();
    }

    public void processBar(Player player){
        new Thread(() -> {

            ServerBossBar bar = ServerBossBar.builder().name(Text.of(barMessage.replace("%m",""+VoteHandler.getMissingVotesCount()).replace("%c",""+VoteHandler.getCurrentVoteCount()).replace("%r",""+VoteHandler.getRequiredVotesCount()).replace("&","\u00a7"))).color(color).overlay(overlay).percent(VoteHandler.getVotePercentage(counterType)/100).build();

            bar.addPlayer(player);
            bar.setVisible(true);

            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            bar.setVisible(false);
            bar.removePlayer(player);

        }).start();
    }

    private static void loadColor(String color){
        switch (color.toUpperCase()){
            case "BLUE": BarHandlerThread.color = BossBarColors.BLUE; return;
            case "GREEN": BarHandlerThread.color = BossBarColors.GREEN; return;
            case "PURPLE": BarHandlerThread.color = BossBarColors.PURPLE; return;
            case "RED": BarHandlerThread.color = BossBarColors.RED; return;
            case "WHITE": BarHandlerThread.color = BossBarColors.WHITE; return;
            case "YELLOW": BarHandlerThread.color = BossBarColors.YELLOW; return;
            default: BarHandlerThread.color = BossBarColors.PINK;
        }
    }

    private static void loadStyle(String Style){
        switch (Style.toUpperCase()){
            case "NOTCHED_6": BarHandlerThread.overlay = BossBarOverlays.NOTCHED_6; return;
            case "NOTCHED_10": BarHandlerThread.overlay = BossBarOverlays.NOTCHED_10; return;
            case "NOTCHED_12": BarHandlerThread.overlay = BossBarOverlays.NOTCHED_12; return;
            case "NOTCHED_20": BarHandlerThread.overlay = BossBarOverlays.NOTCHED_20; return;
            default: BarHandlerThread.overlay = BossBarOverlays.PROGRESS;
        }
    }

}
