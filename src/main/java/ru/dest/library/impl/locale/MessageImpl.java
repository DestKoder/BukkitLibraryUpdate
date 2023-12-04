package ru.dest.library.impl.locale;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.Pair;
import ru.dest.library.object.lang.Message;
import ru.dest.library.utils.ChatUtils;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.StringUtils;

import java.util.List;

public final class MessageImpl implements Message {

    private TextComponent message;

    public MessageImpl(String message){
        this.message = new TextComponent(ColorUtils.parse(message));
    }

    @Override
    public @NotNull Message format(@NotNull String key, @NotNull String value) {
        message = new TextComponent(StringUtils.format(message.getText(), key, value));
        return this;
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        ChatUtils.send(message.getText(), sender);
    }

    @Override
    public Object getRaw() {
        return message;
    }

    @Override
    public void addClickHandler(ClickEvent event){
        message.setClickEvent(event);
    }

    @Override
    public void addHoverEvent(HoverEvent event){
        message.setHoverEvent(event);
    }
}
