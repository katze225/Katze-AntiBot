package me.katze.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.katze.AntiBot;
import me.katze.listener.CaptchaListener;
import me.katze.utility.ColorUtility;
import org.bukkit.command.CommandSender;

@CommandAlias("katze|katze-antibot|guard|antibot")
public class StatsCommand extends BaseCommand {


    @Subcommand("stats")
    @CommandPermission("katze-antibot.stats")
    public void onStats(CommandSender sender) {
        int onCaptchaPlayers = CaptchaListener.onCaptcha.size();
        int captchaPassedPlayers = CaptchaListener.captchaPassed.size();

        sender.sendMessage(ColorUtility.getMsg(AntiBot.getInstance().getConfig().getString("message.command-stats")).replace("{onCaptcha}", String.valueOf(onCaptchaPlayers)).replace("{wellCaptcha}", String.valueOf(captchaPassedPlayers)).replace("{denyCaptcha}", String.valueOf(CaptchaListener.denyCaptcha)));
    }
}