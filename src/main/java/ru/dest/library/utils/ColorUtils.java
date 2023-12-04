package ru.dest.library.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils for working with Colors, Parsing colors etc
 * @since 1.0
 */
public final class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("(&#[0-9a-fA-F]{6})");


    /**
     * Parse color symbols in string
     * @param s - string for parse
     * @return parsed string
     */
    @Contract("_ -> new")
    public static @NotNull String parse(String s){
        Matcher matcher = HEX_PATTERN.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1).substring(1);
            matcher.appendReplacement(sb, "" + getColorFromHexString(hex));
        }
        matcher.appendTail(sb);

        String hexColored = sb.toString();

        return ChatColor.translateAlternateColorCodes('&', hexColored);
    }

    /**
     * Parse color symbols in string list
     * @param l - string list for parse
     * @return return parsed string list
     */
    public static @NotNull List<String> parse(@NotNull List<String> l){
        List<String> result = new ArrayList<>();
        l.forEach(s-> result.add(parse(s)));
        return result;
    }

    /**
     * Remove colors from String
     * @param s string with color codes
     * @return provided string without color codes
     */
    public static String strip(String s){
        return ChatColor.stripColor(parse(s));
    }

    /**
     * Converts hex String to Bukkit Color
     * @param hex {@link String} with hex code (#000000)
     * @return the resulting {@link Color}
     */
    @Contract("_ -> new")
    public static @NotNull Color getColorFromHexString(@NotNull String hex){
        return Color.fromRGB(Integer.valueOf( hex.substring( 1, 3 ), 16 ),
                Integer.valueOf( hex.substring( 3, 5 ), 16 ),
                Integer.valueOf( hex.substring( 5, 7 ), 16 ));
    }
}
