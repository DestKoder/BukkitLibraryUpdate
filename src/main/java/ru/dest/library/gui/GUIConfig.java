package ru.dest.library.gui;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.exception.ItemLoadException;
import ru.dest.library.logging.ConsoleLogger;
import ru.dest.library.logging.ILogger;
import ru.dest.library.object.Pair;
import ru.dest.library.object.gui.ClickHandler;
import ru.dest.library.object.gui.GUIItem;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.StringUtils;
import ru.dest.library.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class GUIConfig {

    private final int size;
    private final String title;

    private final Map<String,Integer> system;
    private final Map<List<Integer>, GUIItem> items;
    private final Map<Integer, ClickHandler> handlers;

    public GUIConfig(@NotNull FileConfiguration cfg, ILogger logger){
        this.size = cfg.getInt("rows")*9;
        this.title = ColorUtils.parse(cfg.getString("title"));
        this.system = new HashMap<>();
        this.items = new HashMap<>();
        this.handlers = new HashMap<>();

        ConfigurationSection system = cfg.getConfigurationSection("system");
        if(system != null){
            system.getKeys(false).forEach(key -> this.system.put(key, system.getInt(key)));
        }

        ConfigurationSection items = cfg.getConfigurationSection("items");

        if(items == null) return;

        items.getKeys(false).forEach(i -> {
            if(!items.isConfigurationSection(i)) {
                logger.warning(ConsoleLogger.RED + "Couldn't load item " + i + " for gui " + cfg.getName() + ": Not a section");
                return;
            }
            ConfigurationSection item = items.getConfigurationSection(i);
            assert item != null;

            if(!item.isSet("slot") && !item.isSet("slots")){
                logger.warning(ConsoleLogger.RED +"Couldn't load item " + i + " for gui " + cfg.getName() + ": No item slot(s) specified");
                return;
            }
            List<Integer> slots;
            if(item.isSet("slots")) slots = item.getIntegerList("slots");
            else slots = Utils.newList(item.getInt("slot"));

            try{
                this.items.put(slots, new GUIItem(item));
            }catch (ItemLoadException e){
                logger.warning(ConsoleLogger.RED + "Couldn't load item " + i + " for gui " + cfg.getName() + ": " + e.getMessage());
            }

            slots.forEach(slot -> handlers.put(slot, new ClickHandler(item)));
        });
    }

    public GUIConfig(int size, String title, Map<String, Integer> system, Map<List<Integer>, GUIItem> items, Map<Integer, ClickHandler> handlers) {
        this.size = size;
        this.title = title;
        this.system = system;
        this.items = items;
        this.handlers = handlers;
    }

    @Contract("_,_ -> new")
    public static @NotNull GUIConfig load(@NotNull File f, ILogger logger) throws IOException {
        if(!f.exists()) throw new FileNotFoundException();
        if(!f.getName().endsWith(".yaml") && !f.getName().endsWith(".yml")) throw new IllegalArgumentException("File must be in yaml format. (.yml / .yaml)");

        return new GUIConfig(YamlConfiguration.loadConfiguration(f), logger);
    }

    public final int getSize() {
        return size;
    }

    public final String getTitle() {
        return title;
    }

    public final @NotNull String getTitle(@NotNull List<Pair<String,String>> format) {
        return StringUtils.format(title, format);
    }

    public final int getSystemHandler(String key){
        return system.getOrDefault(key, -1);
    }

    public final void applyItems(BiConsumer<List<Integer>, GUIItem> c){
        items.forEach(c);
    }

    public final void tryHandle(@NotNull InventoryClickEvent event){
        ClickHandler handler = handlers.get(event.getSlot());
        if(handler == null) return;
        handler.onClick((Player) event.getWhoClicked(), event.getClick());
    }
}
