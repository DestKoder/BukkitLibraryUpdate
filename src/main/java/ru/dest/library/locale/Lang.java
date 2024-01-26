package ru.dest.library.locale;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.impl.locale.LangImpl;
import ru.dest.library.logging.ILogger;
import ru.dest.library.object.lang.Message;
import ru.dest.library.object.lang.Title;
import ru.dest.library.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        cfg.getKeys(true).forEach(s-> {
            if(cfg.isList(s)){
                data.put(s, Utils.collectionToString(cfg.getStringList(s), "\n"));
            }else data.put(s, cfg.getString(s));
        });

        return new LangImpl(data);
    }

    @Contract("_ -> new")
    static @NotNull Lang loadJson(File f) throws IOException {
        Map<String, String> data = new HashMap<>();
        JsonObject obj = new JsonParser().parse(FileUtils.readFileToString(f, StandardCharsets.UTF_8)).getAsJsonObject();

        obj.entrySet().forEach(e -> {
            if(e.getValue().isJsonArray()){
                StringBuilder builder = new StringBuilder();
                for(JsonElement el : e.getValue().getAsJsonArray()){
                    if(el.isJsonPrimitive()) builder.append(el.getAsString());
                    builder.append('\n');
                }
                data.put(e.getKey(), builder.substring(0, builder.toString().length()-1));
            }else {
                if(e.getValue().isJsonPrimitive()){
                    data.put(e.getKey(), e.getValue().getAsString());
                }
            }
        });

        return new LangImpl(data);
    }

    static Lang load(File f, ILogger logger){
        try {
            if(f.getName().endsWith(".json")){
                return loadJson(f);
            }

            if(f.getName().endsWith(".yaml") || f.getName().endsWith(".yml")){
                return loadYaml(f);
            }
        }catch (Exception e){
            logger.warning("Couldn't load lang file " + f.getName());
            logger.error(e);

            return new LangImpl(new HashMap<>());
        }

        throw new IllegalArgumentException("Lang file must be in json or yaml format");
    }
}
