package ru.dest.library.helpers;

import org.jetbrains.annotations.NotNull;
import ru.dest.library.Library;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.utils.Patterns;

import java.util.HashMap;
import java.util.Map;

public class ArgumentValidator {

    private static final Map<String, String> patterns = new HashMap<>();

    static {
        reg("any", Patterns.ANY);
        reg("int", Patterns.INTEGER);
        reg("bool", Patterns.BOOLEAN);
        reg("string", Patterns.ANY);
        reg("ip", Patterns.IP_V4);
        reg("time", Patterns.TIME_UNIT);
        reg("double", Patterns.DOUBLE);
    }

    public static void reg(@NotNull String name, @NotNull String pattern){
        patterns.put(name, pattern);
    }

    public static boolean validate(@NotNull ExecutionData execution, @NotNull String args){
        if(args.trim().isEmpty()) return true;

        String[] data = args.split(" ");
        if(execution.arguments().length < data.length) {
            Library.getInstance().getLang().getMessage("error.notenoughargs").send(execution.executor());
            return false;
        }

        for(int i = 0; i < data.length; i++){
            String tmp = data[i];
            tmp = tmp.substring(1, tmp.length()-1);
            if(!patterns.containsKey(tmp))throw new IllegalStateException("Invalid argument type.");
            if(!execution.arguments()[i].matches(patterns.get(tmp))) {
                Library.getInstance().getLang().getMessage("error.argument."+tmp).format("i", i+"").send(execution.executor());
                return false;
            }
        }
        return true;
    }

}
