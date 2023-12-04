package ru.dest.library.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.Pair;

import java.util.List;

public final class StringUtils {

    @Contract(pure = true)
    public static @NotNull String format(@NotNull String str, String key, String value){
        return str.replaceAll("\\{" + key + "\\}", value);
    }

    public static @NotNull String format(String str, @NotNull Pair<String, String> format){
        return format(str, format.getKey(), format.getValue());
    }

    /**
     * Format a string
     * @param str {@link String} message which will be formatted
     * @param format {@link List} of {@link Pair} of replacements;
     * @return formatted message
     */
    @NotNull
    public static String format(String str, @NotNull List<Pair<String,String>> format){
        for(Pair<String,String> p : format){
            str = format(str, p);
        }
        return str;
    }
}
