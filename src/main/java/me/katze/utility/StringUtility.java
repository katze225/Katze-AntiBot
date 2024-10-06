package me.katze.utility;

import me.katze.AntiBot;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StringUtility {
    private static FileConfiguration config = AntiBot.getInstance().getConfig();

    public static String generateString(String source, int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(source.length());
            result.append(source.charAt(index));
        }

        return result.toString();
    }

    public static String replaceCharacter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String symbol = "#";
        Random random = new Random();
        int index = random.nextInt(input.length());

        return input.substring(0, index) + symbol + input.substring(index + 1);
    }

    public static String convertSymbols(String input) {
        Map<Character, Character> symbolMap = new HashMap<>();

        symbolMap.put('ǫ', 'q');
        symbolMap.put('ᴡ', 'w');
        symbolMap.put('ᴇ', 'e');
        symbolMap.put('ʀ', 'r');
        symbolMap.put('ᴛ', 't');
        symbolMap.put('ʏ', 'y');
        symbolMap.put('ᴜ', 'u');
        symbolMap.put('ᴏ', 'o');
        symbolMap.put('ᴘ', 'p');
        symbolMap.put('ᴀ', 'a');
        symbolMap.put('ꜱ', 's');
        symbolMap.put('ᴅ', 'd');
        symbolMap.put('ꜰ', 'f');
        symbolMap.put('ɡ', 'g');
        symbolMap.put('ʜ', 'h');
        symbolMap.put('ᴊ', 'j');
        symbolMap.put('ᴋ', 'k');
        symbolMap.put('ʟ', 'l');
        symbolMap.put('ᴢ', 'z');
        symbolMap.put('x', 'x');
        symbolMap.put('ᴄ', 'c');
        symbolMap.put('ᴠ', 'v');
        symbolMap.put('ʙ', 'b');
        symbolMap.put('ɴ', 'n');
        symbolMap.put('ᴍ', 'm');

        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            result.append(symbolMap.getOrDefault(c, c));
        }

        return result.toString();
    }
}
