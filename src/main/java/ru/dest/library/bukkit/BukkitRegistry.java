package ru.dest.library.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.command.BukkitCommand;
import ru.dest.library.service.IService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class BukkitRegistry<T extends JavaPlugin> {
    private final T plugin;

    private CommandMap map;

    public BukkitRegistry(T plugin) throws Exception{
        this.plugin = plugin;

        Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);

        map = (CommandMap) field.get(Bukkit.getServer());
    }


    /**
     * Method for registering runtime commands
     * @param cmd {@link BukkitCommand} to register
     */
    public void register(final BukkitCommand<T> cmd){
        map.register(plugin.getDescription().getName(), cmd);
    }

    /**
     * Method for registering several {@link BukkitCommand} in one call
     * @param commands list of commands to register
     */

    @SafeVarargs
    public final void register(BukkitCommand<T> @NotNull ... commands){
        Arrays.stream(commands).forEach(this::register);
    }

    /**
     * Register a BukkitListener
     * @param listener Listener to register
     */
    public void register(BukkitListener<T> listener){
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Register many listeners for one time
     * @param listeners Listeners to register
     */
    @SafeVarargs
    public final void register(BukkitListener<T>... listeners){
        Arrays.stream(listeners).forEach(this::register);
    }


}
