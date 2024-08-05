package me.katze.command;

import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand implements CommandExecutor {
    private FileConfiguration config = AntiBot.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("katze-antibot.reload")) {
                AntiBot.getInstance().reloadPlugin();
                sender.sendMessage(ColorUtility.getMsg(config.getString("message.reload-plugin")));
            } else {
                sender.sendMessage(ColorUtility.getMsg(config.getString("message.no-permission")));
            }
            return true;
        }
        return false;
    }
}
