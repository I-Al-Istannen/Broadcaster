package me.ialistannen.broadcaster.util;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import javafx.util.Pair;

/**
 * Some config Utility methods
 */
public class ConfigUtil {
    public static final BiFunction<String, ConfigurationSection, String> GET_STRING
            = (s, section) -> section.getString(s);
    public static final BiFunction<String, ConfigurationSection, List<String>> GET_STRING_LIST
            = (s, section) -> section.isList(s) ? section.getStringList(s) : null;
    public static final BiFunction<String, ConfigurationSection, Integer> GET_INT
            = (s, section) -> section.isInt(s) ? section.getInt(s) : null;
    public static final BiFunction<String, ConfigurationSection, Long> GET_LONG
            = (s, section) -> section.isLong(s) ? section.getLong(s) : null;
    public static final BiFunction<String, ConfigurationSection, Double> GET_DOUBLE
            = (s, section) -> section.isDouble(s) ? section.getDouble(s) : null;
    public static final BiFunction<String, ConfigurationSection, List<Pair<Enchantment, Integer>>> GET_ENCHANTMENT_LIST
            = (s, section) -> {
        if (!section.isList(s)) {
            return null;
        }
        return null;
    };

    /**
     * @param path The path in the config
     * @param section The Section to get it from
     * @param extractor The extractor function
     * @param <T> The type of the value
     *
     * @return The return value, if any
     */
    public static <T> Optional<T> getFromConfig(String path, ConfigurationSection section,
                                                BiFunction<String, ConfigurationSection, T> extractor) {
        return Optional.ofNullable(extractor.apply(path, section));
    }

    /**
     * @param path The path in the config
     * @param section The Section to get it from
     * @param extractor The extractor function
     * @param <T> The type of the value
     *
     * @return The return value
     *
     * @throws IllegalArgumentException if the value is of a wrong type or missing
     * @see #getFromConfig(String, ConfigurationSection, BiFunction)
     */
    public static <T> T ensureGetFromConfig(String path, ConfigurationSection section,
                                            BiFunction<String, ConfigurationSection, T> extractor) {
        return getFromConfig(path, section, extractor).orElseThrow(() -> new IllegalArgumentException(
                String.format(Locale.ROOT, "Couldn't find the key '%s' or it is of a wrong type.", path)
        ));
    }
}
