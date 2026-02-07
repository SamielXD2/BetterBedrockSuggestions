package com.bettersuggestions;

import org.geysermc.geyser.api.extension.Extension;
import java.util.*;

public class CommandRegistry {
    
    private final Extension extension;
    private final Map<String, CommandInfo> commands = new HashMap<>();

    public CommandRegistry(Extension extension) {
        this.extension = extension;
    }

    public void loadDefaultCommands() {
        // Gamemode commands
        registerCommand("gamemode", "gm",
            Arrays.asList("survival", "creative", "adventure", "spectator", "s", "c", "a", "sp", "0", "1", "2", "3"),
            "Change your gamemode"
        );

        // Teleport commands
        registerCommand("tp", "teleport",
            Arrays.asList("@p", "@a", "@r", "@s", "~ ~ ~", "~1 ~1 ~1"),
            "Teleport to a location or player"
        );

        // Give commands
        registerCommand("give",
            Arrays.asList("@p", "@a", "@s", "diamond", "iron_ingot", "gold_ingot", "emerald", 
                         "diamond_sword", "iron_sword", "bow", "arrow", "cooked_beef", 
                         "golden_apple", "ender_pearl", "elytra", "netherite_ingot"),
            "Give items to players"
        );

        // Effect commands
        registerCommand("effect",
            Arrays.asList("@p", "@a", "@r", "@s", "clear", "speed", "slowness", "haste", 
                         "strength", "instant_health", "jump_boost", "regeneration", 
                         "resistance", "fire_resistance", "water_breathing", "invisibility",
                         "night_vision", "saturation", "glowing", "levitation", "luck"),
            "Apply status effects"
        );

        // Enchant commands
        registerCommand("enchant",
            Arrays.asList("@p", "@a", "@s", "sharpness", "protection", "unbreaking", 
                         "efficiency", "fortune", "silk_touch", "mending", "infinity",
                         "power", "punch", "flame", "looting", "knockback"),
            "Enchant items"
        );

        // Time commands
        registerCommand("time",
            Arrays.asList("set", "add", "query", "day", "night", "noon", "midnight", "0", "6000", "12000", "18000"),
            "Change or query world time"
        );

        // Weather commands
        registerCommand("weather",
            Arrays.asList("clear", "rain", "thunder"),
            "Change weather"
        );

        // Difficulty commands
        registerCommand("difficulty",
            Arrays.asList("peaceful", "easy", "normal", "hard", "p", "e", "n", "h", "0", "1", "2", "3"),
            "Change difficulty"
        );

        // Gamerule commands
        registerCommand("gamerule",
            Arrays.asList("doMobSpawning", "keepInventory", "doFireTick", "doDaylightCycle",
                         "doWeatherCycle", "mobGriefing", "doTileDrops", "doEntityDrops",
                         "commandBlockOutput", "naturalRegeneration", "announceAdvancements",
                         "showDeathMessages", "true", "false"),
            "Change game rules"
        );

        // Kill commands
        registerCommand("kill",
            Arrays.asList("@p", "@a", "@r", "@s", "@e"),
            "Kill entities"
        );

        // XP commands
        registerCommand("xp", "experience",
            Arrays.asList("@p", "@a", "@s", "10", "50", "100", "1000", "10L", "50L"),
            "Give experience"
        );

        // Summon commands
        registerCommand("summon",
            Arrays.asList("minecraft:zombie", "minecraft:skeleton", "minecraft:creeper", 
                         "minecraft:spider", "minecraft:enderman", "minecraft:pig",
                         "minecraft:cow", "minecraft:sheep", "minecraft:chicken",
                         "minecraft:villager", "minecraft:iron_golem", "minecraft:wither",
                         "minecraft:ender_dragon", "~ ~ ~"),
            "Summon entities"
        );

        // Setblock commands
        registerCommand("setblock",
            Arrays.asList("~ ~ ~", "~1 ~1 ~1", "stone", "dirt", "grass_block", 
                         "diamond_block", "gold_block", "iron_block", "air", "water"),
            "Place a block"
        );

        // Fill commands
        registerCommand("fill",
            Arrays.asList("~ ~ ~", "~10 ~10 ~10", "stone", "air", "glass", "replace", "destroy", "keep", "hollow", "outline"),
            "Fill an area with blocks"
        );

        // Clone commands
        registerCommand("clone",
            Arrays.asList("~ ~ ~", "~10 ~10 ~10", "replace", "masked", "filtered"),
            "Clone blocks"
        );

        // Title commands
        registerCommand("title",
            Arrays.asList("@p", "@a", "@r", "@s", "title", "subtitle", "actionbar", "clear", "reset", "times"),
            "Display title screen"
        );

        // Playsound commands
        registerCommand("playsound",
            Arrays.asList("@p", "@a", "@s", "entity.player.levelup", "block.note_block.pling",
                         "entity.experience_orb.pickup", "entity.villager.yes", "entity.villager.no"),
            "Play a sound"
        );

        extension.logger().info("Loaded " + commands.size() + " command suggestions!");
    }

    private void registerCommand(String command, List<String> suggestions, String description) {
        commands.put(command.toLowerCase(), new CommandInfo(command, new ArrayList<>(), suggestions, description));
    }

    private void registerCommand(String command, String alias, List<String> suggestions, String description) {
        List<String> aliases = new ArrayList<>();
        aliases.add(alias);
        CommandInfo info = new CommandInfo(command, aliases, suggestions, description);
        commands.put(command.toLowerCase(), info);
        commands.put(alias.toLowerCase(), info);
    }

    public CommandInfo getCommand(String command) {
        return commands.get(command.toLowerCase());
    }

    public List<String> getSuggestions(String command, String[] args) {
        CommandInfo info = getCommand(command);
        if (info == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = info.getSuggestions();
        
        // If there's a partial argument, filter suggestions
        if (args.length > 0) {
            String partial = args[args.length - 1].toLowerCase();
            List<String> filtered = new ArrayList<>();
            
            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().startsWith(partial)) {
                    filtered.add(suggestion);
                }
            }
            
            return filtered.isEmpty() ? suggestions : filtered;
        }

        return suggestions;
    }

    public static class CommandInfo {
        private final String name;
        private final List<String> aliases;
        private final List<String> suggestions;
        private final String description;

        public CommandInfo(String name, List<String> aliases, List<String> suggestions, String description) {
            this.name = name;
            this.aliases = aliases;
            this.suggestions = suggestions;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public List<String> getAliases() {
            return aliases;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }

        public String getDescription() {
            return description;
        }
    }
}
