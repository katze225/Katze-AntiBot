package me.katze.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProxyUtility {
    public static List<String> load(String link) {
        List<String> ipList = new ArrayList<>();

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String ip = line.split(":")[0].trim();
                    ipList.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipList;
    }
}

