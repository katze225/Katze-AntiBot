package me.katze.utility;

import org.bukkit.ChatColor;

public class ColorUtility {
    public static String getMsg(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}