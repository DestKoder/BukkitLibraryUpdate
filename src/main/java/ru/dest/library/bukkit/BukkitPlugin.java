package ru.dest.library.bukkit;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dest.library.logging.ConsoleLogger;
import ru.dest.library.logging.ILogger;
import ru.dest.library.logging.LoggerWrap;
import ru.dest.library.task.TaskManager;

public abstract class BukkitPlugin<T extends JavaPlugin> extends JavaPlugin{

    protected ILogger logger;
    protected BukkitRegistry<T> registry;
    protected PluginManager pluginManager;

    private final TaskManager taskManager = new TaskManager();

    @Override
    public final void onLoad(){
        this.logger = useWrappedLogger() ? new LoggerWrap(getLogger()) : new ConsoleLogger(getDescription().getName(), false);
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

        try{
            enable();
        }catch (Exception e){
            logger.error(e);
        }
    }

    protected boolean useWrappedLogger(){return false;}

    /**
     * Use: {@link BukkitPlugin#disable}
     */
    @Override
    @Deprecated
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
        taskManager.cancelAll();

        try {
            disable();
        }catch (Exception e){
            logger.error(e);
        }
    }

    public void disable(){}
    public void load(){}
    public abstract void enable();

    public final TaskManager getTaskManager() {
        return taskManager;
    }

    public final PluginManager getPluginManager() {
        return pluginManager;
    }

    public final BukkitRegistry<T> getRegistry() {
        return registry;
    }
}
