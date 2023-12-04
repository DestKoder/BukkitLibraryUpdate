package ru.dest.library.logging;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ConsoleLogger implements ILogger {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private final String pluginName;
    private boolean printErrorStacktrace;

    /**
     * @param pluginName name of Plugin
     * @param printErrorStacktrace need to print error stacktrace?
     */
    public ConsoleLogger(String pluginName, boolean printErrorStacktrace) {
        this.pluginName = pluginName;
        this.printErrorStacktrace = printErrorStacktrace;
    }

    public void info(String msg){
        Bukkit.getLogger().info(CYAN + "["+pluginName+"] " + GREEN + msg + RESET);
    }

    public void warning(String msg) {
        Bukkit.getLogger().warning(CYAN + "["+pluginName+"] " + YELLOW + msg + RESET);
    }

    public void error(@NotNull Exception e) {
        Bukkit.getLogger().warning(CYAN + "["+pluginName+"] " + RED + e.getMessage() + RESET);
        if(printErrorStacktrace) e.printStackTrace();
    }

    public void info(String @NotNull ... msg){
        for(String s : msg) info(s);
    }

    public void warning(String @NotNull ... msg){
        for(String s : msg) warning(s);
    }

    public void error(Exception @NotNull ... ex){
        for(Exception e : ex) error(e);
    }

    public void setPrintErrorStacktrace(boolean printErrorStacktrace) {
        this.printErrorStacktrace = printErrorStacktrace;
    }
}
