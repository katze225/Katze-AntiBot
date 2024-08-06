package me.katze.command;

import me.katze.AntiBot;
import me.katze.listener.CaptchaListener;
import me.katze.utility.ColorUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MainCommand implements CommandExecutor, TabCompleter {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("katze-antibot.admin")) {
            sender.sendMessage(ColorUtility.getMsg(config.getString("message.no-permission")));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("stats")) {

            int onCaptchaPlayers = CaptchaListener.onCaptcha.size();
            int captchaPassedPlayers = CaptchaListener.captchaPassed.size();

            sender.sendMessage(ColorUtility.getMsg(config.getString("message.command-stats")).replace("{onCaptcha}", String.valueOf(onCaptchaPlayers)).replace("{wellCaptcha}", String.valueOf(captchaPassedPlayers)).replace("{denyCaptcha}", String.valueOf(CaptchaListener.denyCaptcha)));
            return true;

        } else {
            sender.sendMessage((ColorUtility.getMsg(config.getString("message.command-usage"))));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            List<String> list = new ArrayList<>();
            list.add("stats");
            return list;
        }
        return null;
    }
}
