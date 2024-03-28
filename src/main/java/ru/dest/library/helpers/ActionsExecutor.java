package ru.dest.library.helpers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.dest.library.impl.locale.TitleImpl;
import ru.dest.library.utils.ChatUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ActionsExecutor {

    private static final Map<String, BiConsumer<Player, String>> actions = new HashMap<>();

    static{
        reg("player", Bukkit::dispatchCommand);
        reg("console", (p,s)-> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
        reg("message", ChatUtils::send);
        reg("title", (p,s) -> {
            String[] data = s.split(";;");
            new TitleImpl(data[0], data[1]).send(p);
        });
    }

    public static void reg(String name, BiConsumer<Player, String> action){
        actions.put(String.format("[%s]", name), action);
    }

    public static void exec(String str, Player p){
        actions.forEach((s, c) -> {
            if(str.startsWith(s)) c.accept(p, str.substring(s.length()).trim());
        });
    }


}
