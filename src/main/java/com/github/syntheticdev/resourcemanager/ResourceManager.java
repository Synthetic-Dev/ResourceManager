package com.github.syntheticdev.resourcemanager;

import com.github.syntheticdev.resourcemanager.events.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class ResourceManager extends JavaPlugin {
    private static ResourceManager plugin;
    private static ArrayList<String> resources;

    public static ResourceManager getPlugin() {
        return plugin;
    }

    public static String getResources() {
        return String.join(",", resources);
    }

    public static void addResource(String resource) {
        if (resources.indexOf(resource) == -1) {
            resources.add(resource);
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerJoinListener(), this);
    }
}
