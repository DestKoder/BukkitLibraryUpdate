package ru.dest.library.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ru.dest.library.command.BukkitCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class BukkitRegistry<T extends JavaPlugin> {
    private final T plugin;

    private final CommandMap map;

    public BukkitRegistry(T plugin) throws Exception{
        this.plugin = plugin;

        Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);

        map = (CommandMap) field.get(Bukkit.getServer());
    }

    @SuppressWarnings("unchecked")
    public void registerAll(String packet){
        Reflections reflections = new Reflections(packet);

        for (Class<?> cl : reflections.getSubTypesOf(BukkitCommand.class)){
            try {
                Constructor<?> constructor = cl.getDeclaredConstructor(plugin.getClass());
                register((BukkitCommand<T>) constructor.newInstance(plugin));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ignored) {}
        }

        for (Class<?> cl : reflections.getSubTypesOf(BukkitListener.class)){
            try {
                Constructor<?> constructor = cl.getDeclaredConstructor(plugin.getClass());
                register((BukkitListener<T>) constructor.newInstance(plugin));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ignored) {}
        }
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
