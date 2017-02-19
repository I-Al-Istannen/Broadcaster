package me.ialistannen.broadcaster.manager;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.broadcaster.config.DelayedMessage;

/**
 * Manages the messages
 */
public class MessageManager {

    private Map<DelayedMessage, LocalDateTime> messages = new LinkedHashMap<>();

    /**
     * @param config The config to read from
     */
    public MessageManager(ConfigurationSection config) {
        LocalDateTime now = LocalDateTime.now();

        MessageSender sender = MessageSender.fromConfig(config.getConfigurationSection("sender"), this);

        ConfigurationSection messages = config.getConfigurationSection("messages");
        for (String key : messages.getKeys(false)) {
            DelayedMessage delayedMessage = DelayedMessage.fromConfig(messages.getConfigurationSection(key));
            this.messages.put(delayedMessage, now);
        }

        sender.start();
    }

    /**
     * @return All the messages that are ready for sending
     */
    List<Entry<DelayedMessage, LocalDateTime>> getReadyMessages() {
        LocalDateTime current = LocalDateTime.now();

        return messages.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isReady(entry.getValue(), current))
                .collect(Collectors.toList());
    }
}
