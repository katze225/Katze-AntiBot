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
        int view_distance = player.getClientViewDistance();

        String ip = String.valueOf(player.getAddress().getAddress()).replace("/", "");

        // Check null LOCALE
        if (locale.equals(null) && config.getBoolean("check.locale.enabled") && !player.hasPermission("katze-antibot.bypass")) {
            player.kickPlayer(ColorUtility.getMsg(config.getString("message.null-locale")));
        }

        // Check null VIEW_DISTANCE
        if ((view_distance == 0 || String.valueOf(view_distance) == null) && config.getBoolean("check.locale.enabled") && !player.hasPermission("katze-antibot.bypass")) {
            player.kickPlayer(ColorUtility.getMsg(config.getString("message.null-view_distance")));
        }

        // Check proxy
        if (AntiBot.proxy.contains(ip) && !player.hasPermission("katze-antibot.bypass")) {
            e.getPlayer().kickPlayer(ColorUtility.getMsg(config.getString("message.use-proxy")));
        }
    }
}
