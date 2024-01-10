package ru.dest.library.command.manager;

import org.bukkit.plugin.java.JavaPlugin;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.ICommand;

public abstract class ObjectManageAction<PLUGIN extends JavaPlugin, OBJECT> implements ICommand {

    protected final PLUGIN plugin;
    private final String name;

    public ObjectManageAction(String name, PLUGIN plugin) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract void perform(ExecutionData execution, OBJECT object);

    public String getName() {
        return name;
    }
}
