# Better Bedrock Suggestions - Geyser Extension

## What Does This Do?

This Geyser extension provides **improved command suggestions** for Bedrock/Mobile players on your Purpur server!

### Features:
✅ Better tab completion for Bedrock players
✅ Suggestions for 20+ common commands (gamemode, tp, give, effect, enchant, etc.)
✅ Smart filtering as you type
✅ Works alongside your existing plugins
✅ No configuration needed - works out of the box!

---

## Installation Instructions

### Step 1: Build the Extension

Since you're on mobile, you'll need to build this on a computer or use GitHub Actions:

**Option A - Using GitHub Actions (Recommended):**
1. Upload this folder to GitHub
2. Create a file `.github/workflows/build.yml` with this content:

```yaml
name: Build Extension

on: [push, workflow_dispatch]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn clean package
      - name: Upload artifact
        uses: actions/upload-artifact@v3
        with:
          name: BetterBedrockSuggestions
          path: target/*.jar
```

3. Go to "Actions" tab on GitHub and run the workflow
4. Download the built .jar file

**Option B - Build on Computer:**
1. Install Java 17+ and Maven
2. Run: `mvn clean package`
3. Find the .jar in the `target/` folder

---

### Step 2: Install on Your Server

1. **Locate your Geyser extensions folder:**
   - Path: `plugins/Geyser-Spigot/extensions/`
   - Create the folder if it doesn't exist

2. **Upload the .jar file:**
   - Put `BetterBedrockSuggestions-1.0.0.jar` into the `extensions/` folder

3. **Restart your server**

4. **Check if it loaded:**
   - Look for this in your console:
   ```
   ====================================
   Better Bedrock Suggestions ENABLED!
   Bedrock players will now have
   improved command suggestions!
   ====================================
   ```

---

## Supported Commands

The extension adds better suggestions for:

- `/gamemode` (gm) - survival, creative, adventure, spectator
- `/tp` (teleport) - players, selectors, coordinates
- `/give` - items like diamond, iron_ingot, diamond_sword, etc.
- `/effect` - all potion effects
- `/enchant` - all enchantments
- `/time` - set, add, day, night, etc.
- `/weather` - clear, rain, thunder
- `/difficulty` - peaceful, easy, normal, hard
- `/gamerule` - all gamerules + true/false
- `/kill` - player selectors
- `/xp` - experience amounts
- `/summon` - entity types
- `/setblock` - block types
- `/fill` - block types and modes
- `/title` - title types
- `/playsound` - common sounds

And more!

---

## How It Works

When a Bedrock player connects:
1. The extension detects they're using Bedrock Edition
2. It intercepts their command input
3. Provides filtered, relevant suggestions
4. Makes commands easier to use on mobile!

---

## Troubleshooting

**Extension not loading?**
- Make sure you're using Geyser-Spigot (not Geyser-Standalone)
- Check that the extensions/ folder exists
- Ensure you're running Geyser 2.0+

**Suggestions not showing?**
- Make sure `command-suggestions: true` in geyser/config.yml
- Restart the server after installing
- Check console for errors

**Still having issues?**
- Check server logs for errors
- Make sure Java 17+ is installed
- Verify Purpur is running 1.21+

---

## Technical Details

- **Version:** 1.0.0
- **Minecraft Version:** 1.21+
- **Server:** Purpur/Paper/Spigot
- **Geyser Version:** 2.0+
- **Java Version:** 17+

---

## Need Help?

If you need help building or installing, you can:
1. Ask in Geyser Discord
2. Post on SpigotMC forums
3. Hire a developer to build it for you

---

## Credits

Created for better Bedrock player experience!
Compatible with Floodgate + Geyser
