package ru.dest.library.logging;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class LoggerWrap implements ILogger {

    private final Logger logger;

    public LoggerWrap(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String msg){
        logger.info(msg);
    }

    @Override
    public void warning(String msg) {
        logger.warning(msg);
    }

    @Override
    public void error(@NotNull Exception e) {
        logger.warning(ConsoleLogger.RED + e.getMessage() + ConsoleLogger.RESET);
        e.printStackTrace();
    }

    @Override
    public void info(String @NotNull ... msg){
        for(String s : msg) info(s);
    }

    @Override
    public void warning(String @NotNull ... msg){
        for(String s : msg) warning(s);
    }

    @Override
    public void error(Exception @NotNull ... ex){
        for(Exception e : ex) error(e);
    }
}
