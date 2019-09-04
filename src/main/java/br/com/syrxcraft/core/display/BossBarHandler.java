package br.com.syrxcraft.core.display;

import br.com.syrxcraft.VoteParty;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BossBarHandler {
    private static BossBar bossBar = new BossBar() {
        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public void setTitle(String title) {

        }

        @Override
        public BarColor getColor() {
            return null;
        }

        @Override
        public void setColor(BarColor color) {

        }

        @Override
        public BarStyle getStyle() {
            return null;
        }

        @Override
        public void setStyle(BarStyle style) {

        }

        @Override
        public void removeFlag(BarFlag flag) {

        }

        @Override
        public void addFlag(BarFlag flag) {

        }

        @Override
        public boolean hasFlag(BarFlag flag) {
            return false;
        }

        @Override
        public void setProgress(double progress) {

        }

        @Override
        public double getProgress() {
            return 0;
        }

        @Override
        public void addPlayer(Player player) {

        }

        @Override
        public void removePlayer(Player player) {

        }

        @Override
        public void removeAll() {

        }

        @Override
        public List<Player> getPlayers() {
            return null;
        }

        @Override
        public void setVisible(boolean visible) {

        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public void show() {

        }

        @Override
        public void hide() {

        }
    };

    public static void broadcast(String message, Double percentage, BarColor color, BarStyle style, Long time){

        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
        bossBar.setTitle(message);
        bossBar.setProgress(percentage);
        bossBar.setStyle(style);
        bossBar.setColor(color);
        bossBar.setVisible(true);

        new BukkitRunnable(){
            @Override
            public void run() {
                bossBar.setVisible(false);
                bossBar.removeAll();
            }
        }.runTaskLaterAsynchronously(VoteParty.getInstance(), time);
    }
}
