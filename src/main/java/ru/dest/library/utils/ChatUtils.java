package ru.dest.library.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static ru.dest.library.utils.ColorUtils.parse;
import static ru.dest.library.utils.Utils.applyPlaceholders;

public final class ChatUtils {

    /**
     * Sends message to CommandSender
     * @param to {@link CommandSender} to whom the message will be sent
     * @param message {@link String} message which will be sended;
     */
    public static void send(@NotNull String message, @NotNull CommandSender to){
        if(to instanceof Player) to.sendMessage(parse(applyPlaceholders(message, (Player) to)).split("\n"));
        else to.sendMessage(parse(message).split("\n"));
    }

    /**
     * Sends basecompontent-message to CommandSender
     * @param sendFor {@link CommandSender} to whom the message will be sent
     * @param message {@link BaseComponent} basecompontent-message which will be sended; Support HoverEffects, ClickableEvent etc.
     */
    public static void sendMessage(@NotNull CommandSender sendFor, BaseComponent message){
        sendFor.spigot().sendMessage(message);
    }

    /**
     * Sends basecompontent-message to Player with message position support
     * @param sendFor {@link Player} to whom the message will be sent
     * @param message {@link BaseComponent} basecompontent-message which will be sended; Support HoverEffects, ClickableEvent etc.
     * @param pos {@link ChatMessageType} position in which message will be showed
     */

    public static void sendMessage(@NotNull Player sendFor, BaseComponent message, ChatMessageType pos){
        sendFor.spigot().sendMessage(pos, message);
    }

    /**
     * Sends message to Player with message position support
     * @param sendFor {@link Player} to whom the message will be sent
     * @param message {@link BaseComponent} message which will be sended; Support HoverEffects, ClickableEvent etc.
     * @param pos {@link ChatMessageType} position in which message will be showed
     */
    public static void sendMessage(@NotNull Player sendFor, String message, ChatMessageType pos){
        sendFor.spigot().sendMessage(pos, new TextComponent(parse(message)));
    }

}
