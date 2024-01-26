package ru.dest.library.object.lang;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public interface IAdvancedMessage {
    
    void addClickHandler(net.md_5.bungee.api.chat.ClickEvent.Action action, String value);
    void addHoverEvent(HoverEvent.Action action, String... value);

    void send(CommandSender sender, ChatMessageType type);

    IAdvancedMessage add(IAdvancedMessage msg);

    TextComponent getRaw();

}
