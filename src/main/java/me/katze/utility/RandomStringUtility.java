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
}
