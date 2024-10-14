package me.katze;

import co.aikar.commands.PaperCommandManager;
import me.katze.command.*;
import me.katze.listener.*;
import me.katze.utility.Metrics;
import me.katze.utility.PlaceholderHook;
import me.katze.utility.ProxyUtility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class AntiBot extends JavaPlugin {
    private static final String LOGO = "/*" +
            "\n█▄▀ ▄▀█ ▀█▀ ▀█ █▀▀ ▄▄ ▄▀█ █▄░█ ▀█▀ █ █▄▄ █▀█ ▀█▀"
            + "\n█░█ █▀█ ░█░ █▄ ██▄ ░░ █▀█ █░▀█ ░█░ █ █▄█ █▄█ ░█░"
            + "\nhttps://discord.gg/6wGy3sYxzw"
            + "\n*/";
    private static final int PLUGIN_ID = 22890;
    public static final String VERSION = "1.3-RELEASE";

    private static AntiBot instance;
    private ProxyUtility proxyUtility;
    private static final Logger LOGGER = Logger.getLogger("Katze-AntiBot");
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();

        LOGGER.info(LOGO);

        loadConfig();

        loadPlaceholderAPI();

        loadProxy();

        loadMetrics();

        loadListeners();

        loadCommands();
    }

    @Override
    public void onDisable() {
        LOGGER.info("Shutdown...");
    }

    public void loadListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new CaptchaListener(), this);
    }

    public void loadCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this.getInstance());

        commandManager.registerCommand(new HelpCommand());
        commandManager.registerCommand(new StatsCommand());
    }

    public void loadMetrics() {
        LOGGER.info("Loading metrics...");
        Metrics metrics = new Metrics(this, PLUGIN_ID);
    }

    public void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            LOGGER.info("Hooking PlaceholderAPI...");
            new PlaceholderHook(this).register();
        } else {
            LOGGER.warning("Error with hooking PlaceholderAPI!");
        }
    }

    public void loadProxy() {
        LOGGER.info("Loading proxy...");
        proxyUtility = new ProxyUtility();

        LOGGER.info("Loaded " + proxyUtility.getCount() + " proxies.");
    }

    public void loadConfig() {
        LOGGER.info("Loading config...");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public static AntiBot getInstance() {
        return instance;
    }

    public ProxyUtility getProxyUtility() {
        return proxyUtility;
    }
}
