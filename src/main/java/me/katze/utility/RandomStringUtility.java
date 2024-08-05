package me.katze.utility;

import java.util.Random;

public class RandomStringUtility {

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

        Random random = new Random();
        int index = random.nextInt(input.length());

        return input.substring(0, index) + '_' + input.substring(index + 1);
    }
}
