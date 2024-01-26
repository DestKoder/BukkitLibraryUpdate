package ru.dest.library.gui;

import org.jetbrains.annotations.NotNull;
import ru.dest.library.logging.ConsoleLogger;
import ru.dest.library.logging.ILogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GUIContainer {

    private final Map<String, GUIConfig> data;

    public GUIContainer(@NotNull File folder, ILogger logger) {
        if(!folder.exists()){
            folder.getParentFile().mkdirs();
            folder.mkdirs();
        }

        this.data = new HashMap<>();

        for(File f : folder.listFiles()){
            if(!f.isFile()) continue;
            if(!f.getName().endsWith(".yaml") || !f.getName().endsWith(".yml")) continue;

            try {
                this.data.put(f.getName().split("\\.")[0], GUIConfig.load(f, logger));
            } catch (IOException e) {
                logger.warning(ConsoleLogger.RED + "Error loading gui " + f.getName()+ ": " + e.getMessage());
            }
        }
    }

    public GUIConfig get(String key){
        return data.get(key);
    }
}
