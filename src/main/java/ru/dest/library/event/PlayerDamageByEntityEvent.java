package ru.dest.library.event;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageByEntityEvent extends EntityDamageByEntityEvent {

    private static final HandlerList handlerList = new HandlerList();

    public PlayerDamageByEntityEvent(@NotNull Entity damager, @NotNull Player damagee, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damager, damagee, cause, damage);
    }

    public Player getPlayer(){
        return (Player) getEntity();
    }

    @NotNull
    @Override
    public HandlerList getHandlers(){
        return handlerList;
    }

    @NotNull
    public static HandlerList getHandlerList(){return handlerList;}
}
