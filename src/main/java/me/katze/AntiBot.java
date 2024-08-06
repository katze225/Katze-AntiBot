package me.katze;

import me.katze.command.MainCommand;
import me.katze.listener.CaptchaListener;
import me.katze.listener.JoinListener;
import me.katze.utility.Metrics;
import me.katze.utility.PlaceholderHook;
import me.katze.utility.ProxyUtility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class AntiBot extends JavaPlugin {

    private static AntiBot instance;
    private static final Logger LOGGER = Logger.getLogger("Katze-AntiBot");
    private FileConfiguration config;

    public static List<String> proxy = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        int pluginId = 22890;

        LOGGER.info("/*");
        LOGGER.info("█▄▀ ▄▀█ ▀█▀ ▀█ █▀▀ ▄▄ ▄▀█ █▄░█ ▀█▀ █ █▄▄ █▀█ ▀█▀");
        LOGGER.info("█░█ █▀█ ░█░ █▄ ██▄ ░░ █▀█ █░▀█ ░█░ █ █▄█ █▄█ ░█░");
        LOGGER.info("*/");

        LOGGER.info("Loading config...");
        config = getConfig();
        loadConfig();

        LOGGER.info("Hooking PlaceholderAPI...");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook(this).register();
        } else {
            LOGGER.warning("Error with hooking PlaceholderAPI!");
        }


        LOGGER.info("Loading metrics...");
        Metrics metrics = new Metrics(this, pluginId);

        LOGGER.info("Loading proxy...");
        for (String url : config.getStringList("check.proxy.list")) {
            List<String> proxies = ProxyUtility.load(url);
            proxy.addAll(proxies);
        }
        
        LOGGER.info("Loaded " + proxy.size() + " proxies.");

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new CaptchaListener(), this);

        this.getCommand("katze-antibot").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        LOGGER.info("Shutdown...");
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void reloadPlugin() {
        reloadConfig();
    }

    public static AntiBot getInstance() {
        return instance;
    }

}
