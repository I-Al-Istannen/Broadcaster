package me.ialistannen.broadcaster.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.ialistannen.broadcaster.util.ConfigUtil;
import me.ialistannen.bukkitutilities.utilities.time.DurationParser;

/**
 * A delayed message
 */
public class DelayedMessage {

    private SoundEffect soundEffect;
    private Duration delay;
    private List<String> messages;

    /**
     * @param soundEffect The {@link SoundEffect} or null if none
     * @param delay The delay
     * @param messages The messages to send
     */
    private DelayedMessage(SoundEffect soundEffect, Duration delay, List<String> messages) {
        this.soundEffect = soundEffect;
        this.delay = delay;
        this.messages = new ArrayList<>(messages);
    }

    /**
     * Sends the message to a player
     *
     * @param player The player to send it to
     */
    public void send(Player player) {
        for (String message : messages) {
            player.sendMessage(message);
        }
        if (soundEffect != null) {
            soundEffect.play(player);
        }
    }

    /**
     * Checks if this message should be send
     *
     * @param last The time of the last execution
     * @param current The current time
     *
     * @return True if the message should be send
     */
    public boolean isReady(LocalDateTime last, LocalDateTime current) {
        return Duration.between(last, current).compareTo(delay) >= 1;
    }

    /**
     * @param section The section to read from
     *
     * @return The created {@link DelayedMessage}
     *
     * @throws IllegalArgumentException if any config option is wrong
     */
    public static DelayedMessage fromConfig(ConfigurationSection section) {
        Objects.requireNonNull(section, "section can not be null!");

        String delayString = ConfigUtil.ensureGetFromConfig("delay", section, ConfigUtil.GET_STRING);
        Duration delay = Duration.ofMillis(DurationParser.parseDuration(delayString));
        List<String> messages = ConfigUtil.getFromConfig("messages", section, ConfigUtil.GET_STRING_LIST)
                .orElse(Collections.emptyList())
                .stream()
                .map(DelayedMessage::color)
                .collect(Collectors.toList());

        SoundEffect soundEffect = null;
        if (section.isSet("sound")) {
            soundEffect = SoundEffect.fromConfig(section.getConfigurationSection("sound"));
        }

        return new DelayedMessage(soundEffect, delay, messages);
    }

    private static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelayedMessage)) {
            return false;
        }
        DelayedMessage that = (DelayedMessage) o;
        return Objects.equals(delay, that.delay) &&
                Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delay, messages);
    }
}
