package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class JoinListener implements Listener {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        String ip = String.valueOf(e.getAddress().getAddress()).replace("/", "");
        String name = e.getName();

        // Находиться ли игрок с таким именем уже на сервере
        if (AntiBot.getInstance().getServer().getOnlinePlayers().contains(name)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ColorUtility.getMsg(config.getString("message.already-online")));
            return;
        }

        // Проверка игрока на использование прокси
        if (AntiBot.proxy.contains(ip)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ColorUtility.getMsg(config.getString("message.use-proxy")));
        }
    }
}
