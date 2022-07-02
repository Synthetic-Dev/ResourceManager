package com.github.syntheticdev.resourcemanager.events;

import com.github.syntheticdev.resourcemanager.ResourceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String resources = ResourceManager.getResources();
        String packUrl = "http://192.248.166.29:3000/spigot-resource-combine?resources=" + resources;

//        Logger logger = ResourceManager.getPlugin().getLogger();

        byte[] sha1 = null;
        try {
            URL url = new URL(packUrl + "&sha1=true");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            if (status < 300) {
                BufferedReader receive = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String base64 = receive.readLine();
                receive.close();
//                logger.info("sha (base64): " + base64);
                sha1 = Base64Coder.decode(base64);
//                logger.info("sha (byte[]): " + sha1 + ", length: " + sha1.length);
            }
            connection.disconnect();
        } catch (Exception err) {
            err.printStackTrace();
        }
        player.setResourcePack(packUrl, sha1, true);
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            player.kickPlayer("Server resource pack has changed or resource pack failed to download.\nPlease rejoin.");
        }
    }
}

