package ru.dest.library.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.object.command.InClassCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandManager<T extends JavaPlugin> extends BukkitCommand<T>  {

    private static final Class<?>[] parameters = new Class[]{ExecutionData.class};

    private final List<SimpleCommand<T>> registeredCommands = new ArrayList<>();

    public CommandManager(T plugin, @NotNull String name,@NotNull String description,@NotNull String usageMessage,@NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases, plugin);
        this.regInClassCommands();
    }

    public CommandManager(T plugin, @NotNull String name) {
        super(name, plugin);
        this.regInClassCommands();
    }

    private void regInClassCommands(){
        for(Method m : getClass().getMethods()){
            if(m.getName().equalsIgnoreCase("__default") || m.getName().equalsIgnoreCase("perform")) continue;
            if(!Arrays.equals(m.getParameterTypes(), parameters)) continue;

            this.registeredCommands.add(new InClassCommand<>(plugin, m.getName(), m, this));
        }
    }

    @Override
    public final void perform(@NotNull ExecutionData execution) throws Exception {
        if(execution.arguments().length < 1){
            this.__default(execution);
            return;
        }

        String[] arguments = new String[execution.arguments().length -1];

        System.arraycopy(execution.arguments(), 1, arguments, 0, execution.arguments().length - 1);

        ExecutionData data = new ExecutionData(execution.executor(), arguments, this, execution.arguments()[0], execution.flags());

        SimpleCommand<T> sub = getRegisteredCommand(execution.arguments()[0]);

        if(sub == null) {
            execution.executor().sendMessage(usageMessage);
            return;
        }

        sub.perform(data);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        List<String> toReturn = new ArrayList<>();
        if(args.length == 1){
            for(SimpleCommand<T> cmd : registeredCommands){
                toReturn.addAll(cmd.getAliases());
                if(!toReturn.contains(cmd.getName())) toReturn.add(cmd.getName());
            }
            return toReturn;
        }
        SimpleCommand<T> sub = getRegisteredCommand(args[0]);

        if(sub == null) return toReturn;

        if (sub instanceof TabCompleter) {
            String[] arguments = new String[args.length - 1];

            System.arraycopy(args, 1, arguments, 0, args.length - 1);

            toReturn.addAll(Objects.requireNonNull(((TabCompleter) sub).onTabComplete(sender, this, alias, arguments)));
        }

        return toReturn;
    }

    protected void __default(@NotNull ExecutionData data){data.executor().sendMessage(usageMessage);}

    protected void addSubCommand(@NotNull SimpleCommand<T> command){
        this.registeredCommands.add(command);
    }

    private @Nullable SimpleCommand<T> getRegisteredCommand(String alias){
        for(SimpleCommand<T> command : registeredCommands){
            if(command.getName().equals(alias) || command.getAliases().contains(alias)) return command;
        }
        return null;
    }
}
