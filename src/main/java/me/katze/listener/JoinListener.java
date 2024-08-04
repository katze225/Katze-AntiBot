package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetAddress;

public class JoinListener implements Listener {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String brand = e.getPlayer().getClientBrandName();
        String locale = e.getPlayer().getLocale();

        InetAddress address = e.getPlayer().getAddress().getAddress();
        String ip = address.getHostAddress();

        if ((brand == null || brand == "") && config.getBoolean("check.brand")) {
            e.getPlayer().kickPlayer(ColorUtility.getMsg(config.getString("message.null-brand")));
        }
        if ((locale == null || locale == "") && config.getBoolean("check.locale")) {
            e.getPlayer().kickPlayer(ColorUtility.getMsg(config.getString("message.null-locale")));
        }
        if (AntiBot.proxy.contains(ip)) {
            e.getPlayer().kickPlayer(ColorUtility.getMsg(config.getString("message.use-proxy")));
        }
    }
}
