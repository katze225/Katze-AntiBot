package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import me.katze.utility.RandomStringUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;


public class CaptchaListener implements Listener {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    private List<Player> onCaptcha = new ArrayList<>();

    // TODO
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (config.getBoolean("check.captcha.enabled")) {
            String random = RandomStringUtility.generateRandomString(config.getString("check.captcha.chars"), config.getInt("check.captcha.length"));
            onCaptcha.add(player);

            Bukkit.getScheduler().runTask(AntiBot.getInstance(), () -> {
                Bukkit.getScheduler().runTaskTimer(AntiBot.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.sendTitle(
                                ColorUtility.getMsg(config.getString("message.captcha-title").replace("{code}", random)),
                                ColorUtility.getMsg(config.getString("message.captcha-subtitle")),
                                0, 20, 0
                        );
                    }
                }, 0L, 20L);

                Bukkit.getScheduler().runTaskLater(AntiBot.getInstance(), () -> {
                    Bukkit.getScheduler().cancelTasks(AntiBot.getInstance());
                    player.kickPlayer(ColorUtility.getMsg(config.getString("message.captcha-kick")));

                }, config.getInt("check.captcha.time") * 20L);
            });
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.contains(player)) {
            onCaptcha.remove(player);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.contains(player)) {
            e.setCancelled(true);
        }
    }


    // TODO
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.contains(player)) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (onCaptcha.contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.contains(player)) {
            e.setCancelled(true);
        }
    }
}
