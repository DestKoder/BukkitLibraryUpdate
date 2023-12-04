package ru.dest.library.bukkit;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dest.library.logging.ConsoleLogger;

public abstract class BukkitPlugin<T extends JavaPlugin> extends JavaPlugin{

    protected ConsoleLogger logger;
    protected BukkitRegistry<T> registry;
    protected PluginManager pluginManager;

    @Override
    public final void onLoad(){
        this.logger = new ConsoleLogger(getDescription().getName(), false);
        load();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        try{
            this.registry = new BukkitRegistry<>((T)this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pluginManager = getServer().getPluginManager();

        enable();
    }

    public void load(){}
    public abstract void enable();

}
