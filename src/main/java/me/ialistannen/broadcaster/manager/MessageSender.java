package me.ialistannen.broadcaster.manager;

import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import me.ialistannen.broadcaster.Broadcaster;
import me.ialistannen.broadcaster.config.DelayedMessage;
import me.ialistannen.broadcaster.util.ConfigUtil;
import me.ialistannen.bukkitutilities.utilities.time.DurationParser;

/**
 * Sends messages
 */
class MessageSender extends BukkitRunnable {

    private MessageManager manager;
    private long delay;

    private MessageSender(MessageManager manager, long delay) {
        this.manager = manager;
        this.delay = delay;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();

        for (Entry<DelayedMessage, LocalDateTime> entry : manager.getReadyMessages()) {
            entry.setValue(now);

            DelayedMessage delayedMessage = entry.getKey();
            Bukkit.getOnlinePlayers().forEach(delayedMessage::send);
        }
    }

    /**
     * Starts this sender
     */
    void start() {
        runTaskTimer(Broadcaster.getInstance(), 0, delay);
    }

    /**
     * Creates a new {@link MessageSender}
     *
     * @param section The section to read from
     * @param messageManager The {@link MessageManager}
     *
     * @return A new {@link MessageSender}
     *
     * @throws IllegalArgumentException if no key named "check-delay" exists in the section
     */
    static MessageSender fromConfig(ConfigurationSection section, MessageManager messageManager) {
        Objects.requireNonNull(section, "section can not be null!");
        Objects.requireNonNull(messageManager, "messageManager can not be null!");

        String delayString = ConfigUtil.ensureGetFromConfig("check-delay", section, ConfigUtil.GET_STRING);

        return new MessageSender(messageManager, DurationParser.parseDurationToTicks(delayString));
    }
}
