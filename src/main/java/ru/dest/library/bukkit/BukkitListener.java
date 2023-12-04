package ru.dest.library.bukkit;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitListener<T extends JavaPlugin> implements Listener {

    protected final T plugin;

    public BukkitListener(T plugin) {
        this.plugin = plugin;
    }
}
