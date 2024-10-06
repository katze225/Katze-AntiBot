package me.katze.listener;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import me.katze.utility.StringUtility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.time.LocalDateTime;

import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;


public class CaptchaListener implements Listener {

    private FileConfiguration config = AntiBot.getInstance().getConfig();

    public static Map<Player, String> onCaptcha = new HashMap<>();
    public static Map<Player, BukkitTask> captchaTask = new HashMap<>();
    public static Map<Player, Integer> captchaAttempt = new HashMap<>();
    public static Map<String, LocalDateTime> captchaPassed = new HashMap<>();

    public static int denyCaptcha = 0;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (config.getBoolean("check.captcha.enabled")
                && !config.getStringList("check.captcha.whitelist-players").contains(player.getName())) {


            if (captchaPassed.containsKey(player.getName())) {
                LocalDateTime lastPassTime = captchaPassed.get(player.getName());
                LocalDateTime now = LocalDateTime.now();
                long minutesSinceLastPass = java.time.Duration.between(lastPassTime, now).toMinutes();
                int whitelistTime = config.getInt("check.captcha.whitelist-time");

                if (minutesSinceLastPass < whitelistTime) {
                    return;
                } else {
                    captchaPassed.remove(player.getName());
                }
            }

            String random = StringUtility.generateString("ǫᴡᴇʀᴛʏᴜɪᴏᴘᴀꜱᴅꜰɢʜᴊᴋʟᴢxᴄᴠʙɴᴍ", config.getInt("check.captcha.length"));
            onCaptcha.put(player, random);
            captchaAttempt.put(player, 3);

            if (onCaptcha.containsKey(player)) {

                if (config.getBoolean("check.captcha.blindness")) {
                    PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 1728000, 0);
                    player.addPotionEffect(blindness);
                }
                player.sendMessage(ColorUtility.getMsg(config.getString("message.captcha-chat-notify")));

                BukkitTask task = Bukkit.getScheduler().runTaskTimer(AntiBot.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        String newRandom = random;
                        if (config.getBoolean("check.captcha.confused")) {
                            newRandom = StringUtility.replaceCharacter(random);
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
                        denyCaptcha += 1;
                    }
                }, config.getInt("check.captcha.time") * 20L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpenEvent(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);
            ColorUtility.getMsg(config.getString("message.captcha-chat-block"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        if (onCaptcha.containsKey(player)) {
            e.setCancelled(true);

            String symbols = StringUtility.convertSymbols(onCaptcha.get(player));

            if (message.contains(symbols)) {
                Bukkit.getScheduler().runTask(AntiBot.getInstance(), () -> {
                    player.sendMessage(ColorUtility.getMsg(config.getString("message.captcha-chat-well")));
                    onCaptcha.remove(player);
                    captchaAttempt.remove(player);

                    captchaPassed.put(player.getName(), LocalDateTime.now());

                    BukkitTask task = captchaTask.remove(player);
                    if (task != null) {
                        Bukkit.getScheduler().cancelTask(task.getTaskId());
                    }


                    if (config.getBoolean("check.captcha.blindness")) {
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
                        denyCaptcha += 1;
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

    @EventHandler
    public void onTeleport(EntityTeleportEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = ((Player) e.getEntity()).getPlayer();

            if (onCaptcha.containsKey(player)) {
                e.setCancelled(true);
            }
        }
    }
}
