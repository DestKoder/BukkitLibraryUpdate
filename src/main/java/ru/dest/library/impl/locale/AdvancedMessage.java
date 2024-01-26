package ru.dest.library.impl.locale;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.lang.IAdvancedMessage;
import ru.dest.library.object.lang.Message;
import ru.dest.library.utils.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdvancedMessage implements IAdvancedMessage {

    private final TextComponent message;

    public AdvancedMessage(@NotNull Message m){
        this.message = new TextComponent(m.getRaw());
    }

    @Override
    public void addClickHandler(ClickEvent.Action action, String value) {
        message.setClickEvent(new ClickEvent(action, value));
    }

    @Override
    public void addHoverEvent(HoverEvent.Action action, String... value) {
        message.setHoverEvent(new HoverEvent(action, toTextComponent(value).toArray(new TextComponent[0])));
    }

    private @NotNull List<TextComponent> toTextComponent(String[] l){
        List<TextComponent> result = new ArrayList<>();
        Arrays.stream(l).forEach(s -> result.add(new TextComponent(s)));
        return result;
    }

    @Override
    public void send(CommandSender sender, ChatMessageType type) {
        if(sender instanceof Player){
            ChatUtils.send((Player) sender, message, type);
        }else
            ChatUtils.send(sender, message.getText());
    }

    @Override
    public IAdvancedMessage add(IAdvancedMessage msg) {
        if(this == msg) return this;

        message.addExtra(msg.getRaw());
        return this;
    }

    @Override
    public TextComponent getRaw() {
        return message;
    }
}
