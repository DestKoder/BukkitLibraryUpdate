package ru.dest.library.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class represents & contains information about Command Execution
 * @since 1.0
 * @author DestKoder
 */
public final class ExecutionData {

    private final CommandSender executor;
    private final String[] arguments;
    private final Command command;
    private final String alias;

    private final Map<String, String> flags;

    public ExecutionData(CommandSender sender, BukkitCommand cmd, String alias, String @NotNull [] args){
        this.executor = sender;
        this.command = cmd;
        this.alias = alias;
        this.flags = new HashMap<>();

        int counter = 0;
        for(String arg : args){
            if(!arg.startsWith("-")) break;
            if(!arg.contains("=")){
                flags.put(arg.substring(1), "true");
                counter++;
                continue;
            }

            String[] data = arg.substring(1).split("=");

            flags.put(data[0], data[1]);
            counter++;
        }

        this.arguments = new String[args.length - counter];
        if (args.length - counter >= 0) System.arraycopy(args, counter, this.arguments, 0, args.length - counter);
    }

    public ExecutionData(CommandSender executor, String[] arguments, Command command, String alias, Map<String, String> flags) {
        this.executor = executor;
        this.arguments = arguments;
        this.command = command;
        this.alias = alias;
        this.flags = flags;
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends CommandSender> T executor(){
        return (T) executor;
    }

    /**
     * Get an arguments passed in command
     * @return Array of arguments
     */
    public String[] arguments(){return arguments;}

    /**
     * Get a Bukkit version of executed command;
     * @return {@link Command}
     */
    public Command command() {return  command;}

    /**
     * Get alias of command with which command was called
     * @return {@link String}
     */
    public String alias(){return alias;}

    /**
     * Check is flag with specified name provided
     * @param flag flag name
     * @return true if provided or false in other cases
     */
    public boolean hasFlag(String flag){
        return flags.containsKey(flag);
    }

    /**
     * Get value of flag with specified name.
     * @param flag flag name
     * @return flag value or null if no flag found by provided name
     */
    @Nullable
    public String getFlagValue(String flag){
        return flags.get(flag);
    }

    @Contract(pure = true)
    public @NotNull @UnmodifiableView Map<String,String> flags(){
        return Collections.unmodifiableMap(flags);
    }
}
