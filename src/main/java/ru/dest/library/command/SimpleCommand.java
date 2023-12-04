package ru.dest.library.command;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface SimpleCommand<T extends JavaPlugin> {

    String getName();

    List<String> getAliases();

    void perform(ExecutionData execution) throws Exception;
}
