package me.ialistannen.broadcaster.config;

import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.ialistannen.broadcaster.util.ConfigUtil;

/**
 * A sound effect
 */
class SoundEffect {

    private String sound;
    private float volume;
    private float pitch;

    /**
     * @param sound The sound name
     * @param volume The volume
     * @param pitch The pitch
     */
    private SoundEffect(String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * @param player The Player to play it for
     */
    void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Creates a {@link SoundEffect} from the section
     *
     * @param section The section to read from
     *
     * @return The created sound effect
     *
     * @throws IllegalArgumentException if no key named "name" exists
     */
    static SoundEffect fromConfig(ConfigurationSection section) {
        Objects.requireNonNull(section, "section can not be null!");
        
        String sound = ConfigUtil.ensureGetFromConfig("name", section, ConfigUtil.GET_STRING);
        float volume = ConfigUtil.getFromConfig("volume", section, ConfigUtil.GET_DOUBLE)
                .orElse(100d)
                .floatValue();
        float pitch = ConfigUtil.getFromConfig("pitch", section, ConfigUtil.GET_DOUBLE)
                .orElse(1d)
                .floatValue();
        return new SoundEffect(sound, volume, pitch);
    }
}
