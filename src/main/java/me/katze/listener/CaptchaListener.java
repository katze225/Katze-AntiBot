package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import me.katze.utility.RandomStringUtility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;


public class CaptchaListener implements Listener {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    private Map<Player, String> onCaptcha = new HashMap<>();
    private Map<Player, BukkitTask> captchaTask = new HashMap<>();
    private Map<Player, Integer> captchaAttempt = new HashMap<>();
    private Map<Player, Long> captchaPassed = new HashMap<>();

    public Map<Player, String> getOnCaptcha() {
        return onCaptcha;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (captchaPassed.containsKey(player) && config.getInt("check.captcha.whitelist-time") != 0) {
            long timeSinceLastPass = System.currentTimeMillis() - captchaPassed.get(player);

            if (timeSinceLastPass < config.getInt("check.captcha.whitelist-time") * 60 * 1000) {
                return;
            }
        }

        if (config.getBoolean("check.captcha.enabled")
                && !config.getStringList("check.captcha.whitelist-always").contains(player.getName())
                && !player.hasPermission("katze-antibot.bypass")) {
            String random = RandomStringUtility.generateRandomString(config.getString("check.captcha.chars"), config.getInt("check.captcha.length"));
            onCaptcha.put(player, random);
            captchaAttempt.put(player, 3);

            if (onCaptcha.containsKey(player)) {

                if (config.getBoolean("check.captcha.give-blindness")) {
                    PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 1728000, 0);
                    player.addPotionEffect(blindness);
                }

                BukkitTask task = Bukkit.getScheduler().runTaskTimer(AntiBot.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        String newRandom = random;
                        if (config.getBoolean("check.captcha.confused")) {
                            newRandom = RandomStringUtility.replaceRandomCharacter(random);
                        }
                        player.sendTitle(
                                ColorUtility.getMsg(config.getString("message.captcha-title").replace("{code}", newRandom)),
                                ColorUtility.getMsg(config.getString("message.captcha-subtitle")),
                                0, 21, 0
                        );
                    }
                }, 0L, 20L);

                captchaTask.put(player, task);

                Bukkit.getScheduler().runTaskLater(AntiBot.getInstance(), () -> {
                    if (onCaptcha.containsKey(player)) {
                        Bukkit.getScheduler().cancelTask(task.getTaskId());
                        onCaptcha.remove(player);
                        captchaAttempt.remove(player);
                        player.kickPlayer(ColorUtility.getMsg(config.getString("message.captcha-kick")));
                    }
                }, config.getInt("check.captcha.time") * 20L);
            }
        }
    }



    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            onCaptcha.remove(player);
            captchaAttempt.remove(player);

            BukkitTask task = captchaTask.remove(player);
            if (task != null) {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
            }
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }



    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);

            if (message.contains(onCaptcha.get(player))) {
                Bukkit.getScheduler().runTask(AntiBot.getInstance(), () -> {
                    player.sendMessage(ColorUtility.getMsg(config.getString("message.captcha-chat-well")));
                    onCaptcha.remove(player);
                    captchaAttempt.remove(player);

                    BukkitTask task = captchaTask.remove(player);
                    if (task != null) {
                        Bukkit.getScheduler().cancelTask(task.getTaskId());
                    }

                    if (config.getInt("check.captcha.whitelist-time") != 0) {
                        captchaPassed.put(player, System.currentTimeMillis());
                    }

                    if (config.getBoolean("check.captcha.give-blindness")) {
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                });
            } else {
                Bukkit.getScheduler().runTask(AntiBot.getInstance(), () -> {
                    captchaAttempt.put(player, captchaAttempt.get(player) - 1);

                    if (captchaAttempt.get(player) <= 0) {
                        BukkitTask task = captchaTask.remove(player);
                        if (task != null) {
                            Bukkit.getScheduler().cancelTask(task.getTaskId());
                        }
                        onCaptcha.remove(player);
                        captchaAttempt.remove(player);
                        player.kickPlayer(ColorUtility.getMsg(config.getString("message.captcha-kick")));
                    } else {
                        player.sendMessage(ColorUtility.getMsg(config.getString("message.captcha-chat-deny").replace("{attempts}", Integer.toString(captchaAttempt.get(player)))));
                    }
                });
            }
        }
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
            ColorUtility.getMsg(config.getString("message.captcha-chat-block"));
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick2(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }
}
