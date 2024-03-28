package ru.dest.library.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Class for calling parallel tasks
 * and guarantees that all tasks will be closed
 * </p>
 * @since 1.0
 * @author DestKoder
 */
public class TaskManager {

    private static TaskManager i;

    private final Map<String, BukkitTask> tasks;

    private JavaPlugin plugin;

    public TaskManager(JavaPlugin plugin){
        this.plugin = plugin;
        this.tasks = new HashMap<>();
    }

    public TaskManager(){
        this.tasks = new HashMap<>();
    }

    @Deprecated
    public static TaskManager get() {
        if(i == null) i = new TaskManager();
        return i;
    }

    /**
     * Call task with specified id
     * @param id task id
     * @param plugin plugin which call task
     * @param async need to call this task async ?
     * @param r {@link BukkitRunnable} which will be called
     */
    public void call(String id, Plugin plugin, boolean async, BukkitRunnable r){
        this.tasks.put(id, async ? r.runTaskAsynchronously(plugin) : r.runTask(plugin));
    }

    /**
     * Call task synchronously with specified id
     * @param id task id
     * @param plugin plugin which call task
     * @param r {@link BukkitRunnable} which will be called
     */
    public void call(String id, Plugin plugin, BukkitRunnable r){
        this.call(id,plugin,false,r);
    }

    /**
     * Call repeating task with specified id
     * @param id task id
     * @param plugin plugin which call task
     * @param async need to call this task async ?
     * @param repeatInTicks in how many tick must action execute
     * @param repeatDelay repeat delay
     * @param r {@link BukkitRunnable} which will be called
     */
    public void callRepeating(String id, Plugin plugin, long repeatInTicks, long repeatDelay, boolean async, BukkitRunnable r){
        this.tasks.put(id, async ? r.runTaskTimerAsynchronously(plugin, repeatDelay, repeatInTicks) : r.runTaskTimer(plugin, repeatDelay, repeatInTicks));
    }

    /**
     * Call repeating task with specified id
     * @param id task id
     * @param plugin plugin which call task
     * @param async need to call this task async ?
     * @param repeatInTicks in how many tick must action execute
     * @param r {@link BukkitRunnable} which will be called
     */
    public void callRepeating(String id, Plugin plugin, long repeatInTicks, boolean async, BukkitRunnable r){
        callRepeating(id, plugin, repeatInTicks, 0L, async, r);
    }

    /**
     * Call repeating task with specified id
     * @param id task id
     * @param plugin plugin which call task
     * @param repeatInTicks in how many tick must action execute
     * @param r {@link BukkitRunnable} which will be called
     */
    public void callRepeating(String id, Plugin plugin, long repeatInTicks, long repeatDelay, BukkitRunnable r){
        callRepeating(id, plugin, repeatInTicks, repeatDelay, false, r);
    }

    /**
     * Call repeating task with specified id
     * @param id task id
     * @param plugin plugin which call task
     * @param repeatInTicks in how many tick must action execute
     * @param r {@link BukkitRunnable} which will be called
     */
    public void callRepeating(String id, Plugin plugin, long repeatInTicks, BukkitRunnable r){
        callRepeating(id, plugin, repeatInTicks, 0L, false, r);
    }

    public void callLater(@NotNull Plugin plugin, long delay, boolean async, BukkitRunnable r){
        this.tasks.put(plugin.getName().toLowerCase() + "_"+ TimeUtils.getCurrentUnixTime(), async ? r.runTaskLaterAsynchronously(plugin, delay) : r.runTaskLater(plugin, delay));
    }

    public void callLater(Plugin plugin, long delay, BukkitRunnable r) {
        callLater(plugin, delay, false, r);
    }

    public void call(@NotNull Plugin plugin, boolean async, BukkitRunnable r){
        this.tasks.put(plugin.getName().toLowerCase() + "_"+ TimeUtils.getCurrentUnixTime(), async ? r.runTaskAsynchronously(plugin) : r.runTask(plugin));
    }

    public void call(Plugin plugin, BukkitRunnable r){
        this.call(plugin,false,r);
    }

    public void callRepeating(@NotNull Plugin plugin, long repeatInTicks, long repeatDelay, boolean async, BukkitRunnable r){
        this.tasks.put(plugin.getName().toLowerCase() + "_"+ TimeUtils.getCurrentUnixTime(), async ? r.runTaskTimerAsynchronously(plugin, repeatDelay, repeatInTicks) : r.runTaskTimer(plugin, repeatDelay, repeatInTicks));
    }

    public void callRepeating(Plugin plugin, long repeatInTicks, boolean async, BukkitRunnable r){
        callRepeating(plugin, repeatInTicks, 0L, async, r);
    }

    public void callRepeating(Plugin plugin, long repeatInTicks, long repeatDelay, BukkitRunnable r){
        callRepeating(plugin, repeatInTicks, repeatDelay, false, r);
    }

    public void callRepeating(Plugin plugin, long repeatInTicks, BukkitRunnable runnable){
        callRepeating(plugin, repeatInTicks, 0L, false, runnable);
    }

    public void callLater(String id, Plugin plugin, long delay, boolean async, BukkitRunnable r){
        this.tasks.put(id, async ? r.runTaskLaterAsynchronously(plugin, delay) : r.runTaskLater(plugin, delay));
    }

    public void callLater(String id, Plugin plugin, long delay, BukkitRunnable r) {
        callLater(id, plugin, delay, false, r);
    }

    public void call(Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        Bukkit.getScheduler().runTask(plugin, r);
    }

    public void callLater(long delay, Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        Bukkit.getScheduler().runTaskLater(plugin, r, delay);
    }

    public void callRepeating(long delay, long time, Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        Bukkit.getScheduler().runTaskTimer(plugin, r, delay, time);
    }

    public void callRepeating(long time, Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        this.callRepeating(0L, time, r);
    }

    public void callAsync(Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, r);
    }

    public void callLaterAsync(long delay, Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, r, delay);
    }

    public void callRepeatingAsync(long delay, long time, Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, r, delay, time);
    }

    public void callRepeatingAsync(long time, Runnable r){
        if(plugin == null) throw new IllegalStateException("Plugin isn't initialzed. Use BukkitPlugin implementations for this method..");
        this.callRepeatingAsync(0L, time, r);
    }


    public void cancel(String id){
        if(!tasks.containsKey(id)) return;

        tasks.get(id).cancel();
        tasks.remove(id);
    }

    public void cancelAll(){
        for(String id  : tasks.keySet()){
            cancel(id);
        }
    }
}
