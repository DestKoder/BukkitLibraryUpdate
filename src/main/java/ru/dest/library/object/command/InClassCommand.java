package ru.dest.library.object.command;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.SimpleCommand;
import ru.dest.library.command.annotation.Aliases;
import ru.dest.library.helpers.AnnotationValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InClassCommand<T extends JavaPlugin> implements SimpleCommand<T> {

    protected final T plugin;
    private final String name;
    @Getter
    private final Method m;
    private final Object manager;
    private final List<String> aliases;

    public InClassCommand(T plugin, String name, @NotNull Method m, Object manager){
        this.plugin = plugin;
        this.name = name;
        this.m = m;
        this.manager = manager;

        Aliases annotation = m.getDeclaredAnnotation(Aliases.class);
        this.aliases = new ArrayList<>();
        if(annotation != null){
            aliases.addAll(Arrays.asList(annotation.aliases()));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void perform(ExecutionData execution) throws Exception {
        try{
            if(!AnnotationValidator.validate(this, execution)) {
                return ;
            }

            m.setAccessible(true);
            m.invoke(manager, execution);
        }catch (Exception e){
            Bukkit.getLogger().warning(ChatColor.RED + "Error executing command " + getName() + ": " + e.getMessage());
            Bukkit.getLogger().warning(ChatColor.RED + "Cause: "+ e.getCause().toString());
        }
    }
}
