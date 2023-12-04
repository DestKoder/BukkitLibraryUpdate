package ru.dest.library.session;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.lang.Message;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static SessionManager instance;

    private SessionManager(){
        instance = this;
    }

    public static @NotNull SessionManager get(){
        return instance == null ? new SessionManager() : instance;
    }

    private final Map<Player, ChatSession> sessions = new HashMap<>();

    public void startSession(Player player, @NotNull Message question, ChatSession session){
        question.send(player);
        //ChatUtils.send(question, player);
        sessions.put(player, session);
    }

    public void removeSession(Player player){
        sessions.remove(player);
    }

    public boolean tryFinish(Player player, String message){
        if(sessions.containsKey(player)){
            sessions.get(player).finish(player,message);
            removeSession(player);
            return true;
        }
        return false;
    }
}
