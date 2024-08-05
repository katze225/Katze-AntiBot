package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinListener implements Listener {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String locale = player.getLocale();
        String ip = String.valueOf(player.getAddress().getAddress()).replace("/", "");

        // Check null locale
        if (locale == null && config.getBoolean("check.locale.enabled")) {
            player.kickPlayer(ColorUtility.getMsg(config.getString("message.null-locale")));
        }
        // Check proxy
        if (AntiBot.proxy.contains(ip)) {
            e.getPlayer().kickPlayer(ColorUtility.getMsg(config.getString("message.use-proxy")));
        }
    }
}
