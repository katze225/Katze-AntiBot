package me.katze.utility;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.katze.AntiBot;
import me.katze.listener.CaptchaListener;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class PlaceholderHook extends PlaceholderExpansion {
    private FileConfiguration config = AntiBot.getInstance().getConfig();
    private static final Logger LOGGER = Logger.getLogger("Katze-AntiBot");
    private final AntiBot plugin;

    public PlaceholderHook(AntiBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "katze225";
    }

    @Override
    public String getIdentifier() {
        return "katze-antibot";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("code")) {

            if (CaptchaListener.onCaptcha.containsKey(player)) {
                return ColorUtility.getMsg(config.getString("message.placeholder-code")).replace("{code}", CaptchaListener.onCaptcha.get(player));
            } else {
                return ColorUtility.getMsg(config.getString("message.placeholder-well"));
            }
        }

        return null;
    }
}
