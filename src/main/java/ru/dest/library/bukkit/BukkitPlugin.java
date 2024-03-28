package ru.dest.library.bukkit;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.locale.Lang;
import ru.dest.library.logging.ConsoleLogger;
import ru.dest.library.logging.ILogger;
import ru.dest.library.logging.LoggerWrap;
import ru.dest.library.object.PluginVersion;
import ru.dest.library.task.TaskManager;

import java.io.File;

public abstract class BukkitPlugin<T extends JavaPlugin> extends JavaPlugin{

    protected ILogger logger;
    protected BukkitRegistry<T> registry;
    protected PluginManager pluginManager;

    private TaskManager taskManager;

    private boolean disable = false;

    @Getter
    private PluginVersion version;

    @Override
    public final void onLoad(){
        this.logger = useWrappedLogger() ? new LoggerWrap(getLogger()) : new ConsoleLogger(getDescription().getName(), false);

        this.version = PluginVersion.fromString(getDescription().getVersion());

        try {
            load();
        } catch (Exception e) {
            logger.error(e);
            disable = true;
            return;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void onEnable() {
        this.pluginManager = getServer().getPluginManager();
        if(disable){
            logger.warning("Error occupied during loading state. Disabling..");
            pluginManager.disablePlugin(this);
            return;
        }
        try{
            this.registry = new BukkitRegistry<>((T)this);
            this.taskManager = new TaskManager(this);
        } catch (Exception e) {
            logger.warning(ConsoleLogger.RED + " Couldn't initialize CommandRegistry:");
            logger.error(e);
            pluginManager.disablePlugin(this);
        }

        try{
            enable();
        }catch (Exception e){
            logger.warning("Exception occupied during enabling state. Disabling");
            pluginManager.disablePlugin(this);
            logger.error(e);
            return;
        }
    }

    protected boolean useWrappedLogger(){return false;}

    /**
     * Use: {@link BukkitPlugin#disable}
     */
    @Override
    @Deprecated
    public void onDisable() {
        HandlerList.unregisterAll(this);
        taskManager.cancelAll();

        try {
            disable();
        }catch (Exception e){
            logger.error(e);
        }
    }

    protected String getLangDir(){
        return "lang";
    }

    protected final @Nullable Lang loadLang(String langFile){
        File lang = new File(getDataFolder(), getLangDir() +"/" + langFile);

        if(!lang.exists()){
            saveResource(getLangDir() +"/" + langFile, true);
            if(!lang.exists()){
               logger.warning("Couldn't load Lang file " + langFile+ ": File doesn't exists");
               disable=true;
               return null;
            }
        }

        return Lang.load(lang, logger);
    }

    public void saveIfNotExists(String resource){
        File f = new File(getDataFolder(), resource);
        if(!f.exists()){
            saveResource(resource, true);
        }
    }
    public void disable() throws Exception{}
    public void load() throws Exception {}
    public abstract void enable() throws Exception;

    public final TaskManager getTaskManager() {
        return taskManager;
    }

    public final PluginManager getPluginManager() {
        return pluginManager;
    }

    public final BukkitRegistry<T> getRegistry() {
        return registry;
    }

    public ILogger logger() {return logger;}

}
