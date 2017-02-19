package me.ialistannen.broadcaster;

import org.bukkit.plugin.java.JavaPlugin;

import me.ialistannen.broadcaster.manager.MessageManager;

public final class Broadcaster extends JavaPlugin {

    private static Broadcaster instance;

    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        new MessageManager(getConfig());
    }

    @Override
    public void onDisable() {
        // prevent the old instance from still being around.
        instance = null;
    }

    /**
     * Returns the plugins instance
     *
     * @return The plugin instance
     */
    public static Broadcaster getInstance() {
        return instance;
    }
}
