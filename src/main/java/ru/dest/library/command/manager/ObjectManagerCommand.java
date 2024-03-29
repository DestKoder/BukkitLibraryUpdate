package ru.dest.library.command.manager;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.command.BukkitCommand;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.SimpleCommand;
import ru.dest.library.helpers.AnnotationValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// /add {player} arg
public abstract class ObjectManagerCommand<PLUGIN extends JavaPlugin, OBJECT> extends BukkitCommand<PLUGIN> {

    private final List<SimpleCommand<PLUGIN>> commands = new ArrayList<>();
    private final List<ObjectManageAction<PLUGIN, OBJECT>> subCommands = new ArrayList<>();

    public ObjectManagerCommand(PLUGIN plugin, String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases, plugin);
    }

    public ObjectManagerCommand(PLUGIN plugin, String name) {
        super(name, plugin);
    }

    private boolean tryExecSubCommand(String name, ExecutionData data) throws Exception {
        SimpleCommand<PLUGIN> cmd = getCommand(name);
        if(cmd == null) return false;
        String[] arguments = new String[data.arguments().length -1];

        System.arraycopy(data.arguments(), 1, arguments, 0, data.arguments().length - 1);
        ExecutionData d = new ExecutionData(data.executor(), arguments, this, name, data.flags());

        if(!AnnotationValidator.validate(cmd, d)){
            return true;
        }

        cmd.perform(d);
        return true;
    }

    @Override
    public final void perform(@NotNull ExecutionData execution) throws Exception {
        if(execution.arguments().length == 0){
            this._default(execution);
            return;
        }

        boolean res = tryExecSubCommand(execution.argument(0), execution);

        if(!res && execution.arguments().length == 1){
            this._default(execution);
            return;
        }

        if(res) return;

        String[] arguments = new String[execution.arguments().length -2];

        System.arraycopy(execution.arguments(), 2, arguments, 0, execution.arguments().length - 2);

        ExecutionData data = new ExecutionData(execution.executor(), arguments, this, execution.arguments()[0], execution.flags());


        System.out.println("Trying to find command " + execution.arguments()[1]);
        ObjectManageAction<PLUGIN, OBJECT> sub = getAction(execution.arguments()[1]);

        if(sub == null) {
            execution.executor().sendMessage(usageMessage);
            return;
        }

        if(AnnotationValidator.validate(sub, data)) sub.perform(data, get(execution.arguments()[0]));
        else return;
    }

    @NotNull
    @Override
    public final List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        List<String> toReturn = new ArrayList<>();
        if(args.length == 1){
            toReturn.addAll(availableValues());
            for(SimpleCommand<PLUGIN> cmd : commands){
                toReturn.addAll(cmd.getAliases());
            }
            return toReturn;
        }

        if(args.length == 2){
            for(ObjectManageAction<PLUGIN, OBJECT> cmd : subCommands){
                if(!toReturn.contains(cmd.getName())) toReturn.add(cmd.getName());
            }
            SimpleCommand<PLUGIN> sub = getCommand(args[0]);

            if(sub == null) return toReturn;

            if (sub instanceof TabCompleter) {
                String[] arguments = new String[args.length - 1];

                System.arraycopy(args, 1, arguments, 0, args.length - 1);

                toReturn.addAll(Objects.requireNonNull(((TabCompleter) sub).onTabComplete(sender, this, alias, arguments)));
            }
            return toReturn;
        }
        ObjectManageAction<PLUGIN, OBJECT> sub = getAction(args[0]);

        if(sub == null) return toReturn;

        if (sub instanceof TabCompleter) {
            String[] arguments = new String[args.length - 1];

            System.arraycopy(args, 1, arguments, 0, args.length - 1);

            toReturn.addAll(Objects.requireNonNull(((TabCompleter) sub).onTabComplete(sender, this, alias, arguments)));
        }

        return toReturn;
    }

    protected abstract List<String> availableValues();

    protected abstract OBJECT get(String arg);

    public void _default(@NotNull ExecutionData executionData){
        executionData.executor().sendMessage(usageMessage);
    }

    protected final void addCommand(SimpleCommand<PLUGIN> cmd){
        this.commands.add(cmd);
    }

    protected final void addAction(@NotNull ObjectManageAction<PLUGIN, OBJECT> cmd){
        if(getAction(cmd.getName()) != null) throw new IllegalArgumentException("Action with name " + cmd.getName() + " already registered.");
        this.subCommands.add(cmd);
    }

    private @Nullable SimpleCommand<PLUGIN> getCommand(String name){
        for(SimpleCommand<PLUGIN> cmd : commands){
            if(cmd.getName().equalsIgnoreCase(name) || cmd.getAliases().contains(name)) return cmd;
        }
        return null;
    }

    private @Nullable ObjectManageAction<PLUGIN, OBJECT> getAction(String name){
        for(ObjectManageAction<PLUGIN, OBJECT> action : subCommands){
            if(action.getName().equalsIgnoreCase(name)) return action;
        }
        return null;
    }
}
