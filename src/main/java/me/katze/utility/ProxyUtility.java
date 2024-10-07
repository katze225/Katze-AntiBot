package me.katze.utility;

import me.katze.AntiBot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProxyUtility {
    private static List<String> proxy = new ArrayList<>();

    private void load() {
        List<String> ipList = new ArrayList<>();

        for (String url : AntiBot.getInstance().getConfig().getStringList("check.proxy.list")) {
            try {
                URL link = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) link.openConnection();
                connection.setRequestMethod("GET");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String ip = line.split(":")[0].trim();
                        ipList.add(ip);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Cannot load from: " + url);
            }
        }
        proxy.addAll(ipList);
    }

    public ProxyUtility() {
        load();
    }

    public boolean isProxy(String ip) {
        return proxy.contains(ip);
    }

    public Integer getCount() {
        return proxy.size();
    }
}

