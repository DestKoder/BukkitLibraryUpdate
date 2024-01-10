package ru.dest.library.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.helpers.AnnotationValidator;

import java.util.ArrayList;
import java.util.List;

public abstract class BukkitCommand<T extends JavaPlugin> extends Command implements SimpleCommand<T>{

    protected final T plugin;

    public BukkitCommand(String name, String description, String usageMessage, List<String> aliases, T plugin) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
    }

    public BukkitCommand(String name, T plugin){
        super(name, "Some bukkit command", "/"+name, new ArrayList<>());
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        ExecutionData data = new ExecutionData(sender, this, alias, args);

        try{
            if(!AnnotationValidator.validate(this, data)) {
                return true;
            }

            this.perform(data);
        }catch (Exception e){
            Bukkit.getLogger().warning(ChatColor.RED + "Error executing command " + getName() + ": " + e.getMessage());
            e.printStackTrace();
            //            Bukkit.getLogger().warning(ChatColor.RED + "Cause: "+ e.getCause().toString());
        }
        return true;
    }

}
