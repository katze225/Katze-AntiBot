package me.katze.utility;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtility {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String getMsg(String str) {
        String translated = ChatColor.translateAlternateColorCodes('&', str);

        Matcher matcher = HEX_PATTERN.matcher(translated);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            StringBuilder hex = new StringBuilder("§x");
            for (char c : hexCode.toCharArray()) {
                hex.append('§').append(c);
            }
            matcher.appendReplacement(buffer, hex.toString());
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}