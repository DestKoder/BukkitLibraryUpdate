package ru.dest.library.command;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface SimpleCommand<T extends JavaPlugin> extends ICommand{
    void perform(ExecutionData execution) throws Exception;
}
