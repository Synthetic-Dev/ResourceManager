package com.github.syntheticdev.resourcemanager.events;

import com.github.syntheticdev.resourcemanager.ResourceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HexFormat;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String resources = ResourceManager.getResources();
        String apiUrl = "http://192.248.166.29:3000/spigot-resources/";

        String sha1Hex = null;
        byte[] sha1 = null;
        try {
            URL url = new URL(apiUrl + "sha1?resources=" + resources);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            if (status < 300) {
                BufferedReader receive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sha1Hex = receive.readLine();
                sha1 = HexFormat.of().parseHex(sha1Hex);
                receive.close();
            }
            connection.disconnect();
        } catch (Exception err) {
            err.printStackTrace();
        }
        player.setResourcePack(apiUrl + "get/" + sha1Hex, sha1, true);
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            player.kickPlayer("Server resource pack has changed or resource pack failed to download.\nPlease rejoin.");
        }
    }
}

