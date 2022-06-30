package com.github.syntheticdev.resourcemanager.events;

import com.github.syntheticdev.resourcemanager.ResourceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String resources = ResourceManager.getResources();
        player.setResourcePack("http://192.248.166.29:3000/spigot-resource-combine?resources=" + resources, null, true);
    }
}

