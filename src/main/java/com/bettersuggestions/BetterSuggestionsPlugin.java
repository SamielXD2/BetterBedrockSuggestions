package com.bettersuggestions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.SessionLoginEvent;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BetterSuggestionsPlugin extends JavaPlugin implements EventRegistrar, Listener {

    private final Map<UUID, GeyserConnection> bedrockPlayers = new ConcurrentHashMap<>();
    private CommandRegistry commandRegistry;
    private GeyserApi geyserApi;

    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled("Geyser-Spigot")) {
            getLogger().severe("Geyser-Spigot is not installed! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            this.geyserApi = GeyserApi.api();
            
            geyserApi.eventBus().subscribe(this, SessionLoginEvent.class, this::onSessionLogin);
            geyserApi.eventBus().subscribe(this, SessionDisconnectEvent.class, this::onSessionDisconnect);
            geyserApi.eventBus().subscribe(this, GeyserDefineCommandsEvent.class, this::onDefineCommands);
            
            getServer().getPluginManager().registerEvents(this, this);
            
            this.commandRegistry = new CommandRegistry(getLogger());
            commandRegistry.loadDefaultCommands();
            
            getLogger().info("====================================");
            getLogger().info("Better Bedrock Suggestions ENABLED!");
            getLogger().info("Commands will auto-open menus!");
            getLogger().info("====================================");
        } catch (Exception e) {
            getLogger().severe("Failed to initialize plugin: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void onSessionLogin(SessionLoginEvent event) {
        Bukkit.getScheduler().runTask(this, () -> {
            GeyserConnection connection = event.connection();
            bedrockPlayers.put(connection.javaUuid(), connection);
            getLogger().info("Bedrock player connected: " + connection.javaUsername());
        });
    }

    public void onSessionDisconnect(SessionDisconnectEvent event) {
        Bukkit.getScheduler().runTask(this, () -> {
            GeyserConnection connection = event.connection();
            bedrockPlayers.remove(connection.javaUuid());
            getLogger().info("Bedrock player disconnected: " + connection.javaUsername());
        });
    }

    public void onDefineCommands(GeyserDefineCommandsEvent event) {
        // Register custom Geyser commands that trigger menus
        event.register(Command.builder(this)
            .name("gamemode")
            .description("Change gamemode with menu")
            .aliases(Arrays.asList("gm"))
            .bedrockOnly(true)
            .executableOnConsole(false)
            .executor((source, command, args) -> {
                if (source.isConsole()) {
                    return;
                }
                
                // Get player UUID and show menu
                UUID uuid = getPlayerUUID(source);
                if (uuid != null) {
                    showGamemodeMenu(uuid);
                }
            })
            .build());
            
        event.register(Command.builder(this)
            .name("give")
            .description("Give items with menu")
            .bedrockOnly(true)
            .executableOnConsole(false)
            .executor((source, command, args) -> {
                UUID uuid = getPlayerUUID(source);
                if (uuid != null) {
                    showGiveMenu(uuid);
                }
            })
            .build());
            
        event.register(Command.builder(this)
            .name("effect")
            .description("Apply effects with menu")
            .bedrockOnly(true)
            .executableOnConsole(false)
            .executor((source, command, args) -> {
                UUID uuid = getPlayerUUID(source);
                if (uuid != null) {
                    showEffectMenu(uuid);
                }
            })
            .build());
            
        event.register(Command.builder(this)
            .name("time")
            .description("Change time with menu")
            .bedrockOnly(true)
            .executableOnConsole(false)
            .executor((source, command, args) -> {
                UUID uuid = getPlayerUUID(source);
                if (uuid != null) {
                    showTimeMenu(uuid);
                }
            })
            .build());
            
        event.register(Command.builder(this)
            .name("weather")
            .description("Change weather with menu")
            .bedrockOnly(true)
            .executableOnConsole(false)
            .executor((source, command, args) -> {
                UUID uuid = getPlayerUUID(source);
                if (uuid != null) {
                    showWeatherMenu(uuid);
                }
            })
            .build());
            
        event.register(Command.builder(this)
            .name("tp")
            .description("Teleport with menu")
            .aliases(Arrays.asList("teleport"))
            .bedrockOnly(true)
            .executableOnConsole(false)
            .executor((source, command, args) -> {
                UUID uuid = getPlayerUUID(source);
                if (uuid != null) {
                    showTeleportMenu(uuid);
                }
            })
            .build());
    }

    private UUID getPlayerUUID(CommandSource source) {
        GeyserConnection connection = source.connection();
        if (connection != null) {
            return connection.javaUuid();
        }
        return null;
    }

    private void showGamemodeMenu(UUID playerUUID) {
        GeyserConnection connection = bedrockPlayers.get(playerUUID);
        if (connection == null) return;
        
        SimpleForm.Builder form = SimpleForm.builder()
            .title("§l§eGamemode")
            .content("§7Choose a gamemode:");
        
        form.button("§2Survival");
        form.button("§cCreative");
        form.button("§6Adventure");
        form.button("§9Spectator");
        
        form.validResultHandler(response -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            
            String[] modes = {"survival", "creative", "adventure", "spectator"};
            player.performCommand("gamemode " + modes[response.clickedButtonId()]);
        });
        
        connection.sendForm(form);
    }

    private void showGiveMenu(UUID playerUUID) {
        GeyserConnection connection = bedrockPlayers.get(playerUUID);
        if (connection == null) return;
        
        SimpleForm.Builder form = SimpleForm.builder()
            .title("§l§eGive Item")
            .content("§7Select an item:");
        
        form.button("§bDiamond x64");
        form.button("§7Iron Ingot x64");
        form.button("§6Gold Ingot x64");
        form.button("§aEmerald x64");
        form.button("§cDiamond Sword");
        form.button("§3Diamond Pickaxe");
        form.button("§9Diamond Axe");
        form.button("§5Bow");
        form.button("§fArrow x64");
        form.button("§eGolden Apple x16");
        
        form.validResultHandler(response -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            
            String[] items = {
                "minecraft:diamond 64",
                "minecraft:iron_ingot 64",
                "minecraft:gold_ingot 64",
                "minecraft:emerald 64",
                "minecraft:diamond_sword",
                "minecraft:diamond_pickaxe",
                "minecraft:diamond_axe",
                "minecraft:bow",
                "minecraft:arrow 64",
                "minecraft:golden_apple 16"
            };
            
            player.performCommand("give @s " + items[response.clickedButtonId()]);
        });
        
        connection.sendForm(form);
    }

    private void showEffectMenu(UUID playerUUID) {
        GeyserConnection connection = bedrockPlayers.get(playerUUID);
        if (connection == null) return;
        
        SimpleForm.Builder form = SimpleForm.builder()
            .title("§l§eEffects")
            .content("§7Choose an effect:");
        
        form.button("§fSpeed II (1 min)");
        form.button("§cStrength II (1 min)");
        form.button("§aJump Boost II (1 min)");
        form.button("§dRegeneration II (1 min)");
        form.button("§6Night Vision (5 min)");
        form.button("§bWater Breathing (3 min)");
        form.button("§7Invisibility (3 min)");
        form.button("§eHaste II (1 min)");
        form.button("§4§lCLEAR ALL EFFECTS");
        
        form.validResultHandler(response -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            
            int id = response.clickedButtonId();
            
            if (id == 8) {
                player.performCommand("effect clear @s");
            } else {
                String[] effects = {
                    "effect give @s minecraft:speed 60 1",
                    "effect give @s minecraft:strength 60 1",
                    "effect give @s minecraft:jump_boost 60 1",
                    "effect give @s minecraft:regeneration 60 1",
                    "effect give @s minecraft:night_vision 300 0",
                    "effect give @s minecraft:water_breathing 180 0",
                    "effect give @s minecraft:invisibility 180 0",
                    "effect give @s minecraft:haste 60 1"
                };
                player.performCommand(effects[id]);
            }
        });
        
        connection.sendForm(form);
    }

    private void showTimeMenu(UUID playerUUID) {
        GeyserConnection connection = bedrockPlayers.get(playerUUID);
        if (connection == null) return;
        
        SimpleForm.Builder form = SimpleForm.builder()
            .title("§l§eTime")
            .content("§7Set the time:");
        
        form.button("§eMorning (Day)");
        form.button("§6Noon");
        form.button("§cSunset");
        form.button("§9Night");
        form.button("§5Midnight");
        
        form.validResultHandler(response -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            
            String[] times = {"day", "noon", "sunset", "night", "midnight"};
            player.performCommand("time set " + times[response.clickedButtonId()]);
        });
        
        connection.sendForm(form);
    }

    private void showWeatherMenu(UUID playerUUID) {
        GeyserConnection connection = bedrockPlayers.get(playerUUID);
        if (connection == null) return;
        
        SimpleForm.Builder form = SimpleForm.builder()
            .title("§l§eWeather")
            .content("§7Set the weather:");
        
        form.button("§bClear");
        form.button("§7Rain");
        form.button("§8Thunder");
        
        form.validResultHandler(response -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            
            String[] weather = {"clear", "rain", "thunder"};
            player.performCommand("weather " + weather[response.clickedButtonId()]);
        });
        
        connection.sendForm(form);
    }

    private void showTeleportMenu(UUID playerUUID) {
        GeyserConnection connection = bedrockPlayers.get(playerUUID);
        if (connection == null) return;
        
        SimpleForm.Builder form = SimpleForm.builder()
            .title("§l§eTeleport")
            .content("§7Quick teleports:");
        
        form.button("§aSpawn");
        form.button("§bWorld Spawn");
        
        form.validResultHandler(response -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            
            if (response.clickedButtonId() == 0) {
                player.performCommand("spawn");
            } else {
                player.performCommand("tp @s " + 
                    player.getWorld().getSpawnLocation().getX() + " " +
                    player.getWorld().getSpawnLocation().getY() + " " +
                    player.getWorld().getSpawnLocation().getZ());
            }
        });
        
        connection.sendForm(form);
    }

    public boolean isBedrockPlayer(UUID uuid) {
        return bedrockPlayers.containsKey(uuid);
    }

    @Override
    public void onDisable() {
        bedrockPlayers.clear();
        getLogger().info("Better Bedrock Suggestions disabled!");
    }
}
