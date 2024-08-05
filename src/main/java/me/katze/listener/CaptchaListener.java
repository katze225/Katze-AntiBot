package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.RandomStringUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;


public class CaptchaListener implements Listener {
    private FileConfiguration config = AntiBot.getInstance().getConfig();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.getBoolean("check.captcha.enabled")) {
            String random = RandomStringUtility.generateRandomString(config.getString("check.captcha.chars"), config.getInt("check.captcha.length"));

        }
    }
}
