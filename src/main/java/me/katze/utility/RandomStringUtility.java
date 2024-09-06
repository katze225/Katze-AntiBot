package me.katze.utility;

import me.katze.AntiBot;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

public class RandomStringUtility {
    private static FileConfiguration config = AntiBot.getInstance().getConfig();

    public static String generateRandomString(String source, int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(source.length());
            result.append(source.charAt(index));
        }

        return result.toString();
    }

    public static String replaceRandomCharacter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Random char
        Random random = new Random();
        int index1 = random.nextInt(input.length());

        // Random Symbol
        int random2 = random.nextInt(config.getStringList("check.captcha.symbol").size());
        String randomSymbol = config.getStringList("check.captcha.symbol").get(random2);

        return input.substring(0, index1) + randomSymbol + input.substring(index1 + 1);
    }
}
