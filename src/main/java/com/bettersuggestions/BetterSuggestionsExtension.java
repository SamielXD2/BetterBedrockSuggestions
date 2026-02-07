package com.bettersuggestions;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.bedrock.SessionInitializeEvent;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BetterSuggestionsExtension implements Extension {

    private final Map<UUID, GeyserConnection> bedrockPlayers = new ConcurrentHashMap<>();
    private CommandRegistry commandRegistry;

    public void onEnable() {
        this.commandRegistry = new CommandRegistry(this);
        commandRegistry.loadDefaultCommands();
        
        this.logger().info("====================================");
        this.logger().info("Better Bedrock Suggestions ENABLED!");
        this.logger().info("Bedrock players will now have");
        this.logger().info("improved command suggestions!");
        this.logger().info("====================================");
    }

    @Subscribe
    public void onSessionInitialize(SessionInitializeEvent event) {
        GeyserConnection connection = event.connection();
        bedrockPlayers.put(connection.javaUuid(), connection);
        this.logger().info("Bedrock player connected: " + connection.javaUsername());
    }

    @Subscribe
    public void onSessionDisconnect(SessionDisconnectEvent event) {
        GeyserConnection connection = event.connection();
        bedrockPlayers.remove(connection.javaUuid());
        this.logger().info("Bedrock player disconnected: " + connection.javaUsername());
    }

    public boolean isBedrockPlayer(UUID uuid) {
        return bedrockPlayers.containsKey(uuid);
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public void onDisable() {
        this.logger().info("Better Bedrock Suggestions disabled!");
    }
}
