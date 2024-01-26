package ru.dest.library.impl.locale;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.object.lang.Message;
import ru.dest.library.utils.ChatUtils;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.StringUtils;

public final class MessageImpl implements Message {

    private String message;

    public MessageImpl(String message){
        this.message = ColorUtils.parse(message);
    }

    @Override
    public @NotNull Message format(@NotNull String key, @NotNull String value) {
        message = StringUtils.format(message, key, value);
        return this;
    }

    @Override
    public void broadcast() {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void broadcast(String permission) {
        Bukkit.broadcast(message, permission);
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        ChatUtils.send(sender, message);
    }

    @Override
    public String getRaw() {
        return message;
    }
}
