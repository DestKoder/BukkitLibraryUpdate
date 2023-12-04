package ru.dest.library.locale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.impl.locale.LangImpl;
import ru.dest.library.object.lang.Message;
import ru.dest.library.object.lang.Title;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class represents a Localization loaded from file
 * @since 1.0
 * @author DestKoder
 */
public interface Lang {

    @NotNull
    String getValue(@NotNull String key);

    @NotNull
    Message getMessage(@NotNull String key);

    @NotNull
    Title getTitle(@NotNull String key);

    @Contract("_ -> new")
    static @NotNull Lang loadYaml(File f){
        Map<String, String> data = new HashMap<>();
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        cfg.getKeys(true).forEach(s-> data.put(s, cfg.getString(s)));

        return new LangImpl(data);
    }
}
