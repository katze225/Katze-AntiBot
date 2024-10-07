package me.katze.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.katze.AntiBot;
import me.katze.utility.ColorUtility;
import org.bukkit.command.CommandSender;

@CommandAlias("katze|katze-antibot|guard|antibot")
public class HelpCommand extends BaseCommand {

    @Default
    @Subcommand("help")
    @CommandPermission("katze-antibot.help")
    public void onHelp(CommandSender sender) {
        for (String string : AntiBot.getInstance().getConfig().getStringList("message.command-help")) {
            string = ColorUtility.getMsg(string).replace("{version}", AntiBot.VERSION);
            sender.sendMessage(string);
        }
    }
}