package ru.dest.library.object.lang;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.Pair;

import java.util.List;

public interface Message {

    @NotNull
    Message format(@NotNull String key, @NotNull String value);

    @NotNull
    default Message format(@NotNull Pair<String,String> replace){
        return format(replace.getKey(), replace.getValue());
    }

    @NotNull
    default Message format(@NotNull List<Pair<String,String>> replace){
        Message m = this;
        for(Pair<String,String> p : replace){
            m = format(p);
        }
        return m;
    }

    void send(@NotNull CommandSender sender);

    String getRaw();

    //
//    void addClickHandler(ClickEvent event);
//
//    void addHoverEvent(HoverEvent event);
}
