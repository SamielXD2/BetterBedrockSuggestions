package com.bettersuggestions;

import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.bedrock.SessionInitializeEvent;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.event.subscribe.Subscribe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BetterSuggestionsPlugin extends JavaPlugin {

    private final Map<UUID, GeyserConnection> bedrockPlayers = new ConcurrentHashMap<>();
    private CommandRegistry commandRegistry;
    private GeyserApi geyserApi;

    @Override
    public void onEnable() {
        // Check if Geyser is available
        if (!getServer().getPluginManager().isPluginEnabled("Geyser-Spigot")) {
            getLogger().severe("Geyser-Spigot is not installed! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Get Geyser API
        this.geyserApi = GeyserApi.api();
        
        // Register event listeners
        geyserApi.eventBus().subscribe(this, SessionInitializeEvent.class, this::onSessionInitialize);
        geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionDisconnect);
        
        // Initialize command registry
        this.commandRegistry = new CommandRegistry(this);
        commandRegistry.loadDefaultCommands();
        
        getLogger().info("====================================");
        getLogger().info("Better Bedrock Suggestions ENABLED!");
        getLogger().info("Bedrock players will now have");
        getLogger().info("improved command suggestions!");
        getLogger().info("====================================");
    }

    @Subscribe
    public void onSessionInitialize(SessionInitializeEvent event) {
        GeyserConnection connection = event.connection();
        bedrockPlayers.put(connection.javaUuid(), connection);
        getLogger().info("Bedrock player connected: " + connection.javaUsername());
    }

    @Subscribe
    public void onSessionDisconnect(SessionDisconnectEvent event) {
        GeyserConnection connection = event.connection();
        bedrockPlayers.remove(connection.javaUuid());
        getLogger().info("Bedrock player disconnected: " + connection.javaUsername());
    }

    public boolean isBedrockPlayer(UUID uuid) {
        return bedrockPlayers.containsKey(uuid);
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @Override
    public void onDisable() {
        getLogger().info("Better Bedrock Suggestions disabled!");
    }
          }
