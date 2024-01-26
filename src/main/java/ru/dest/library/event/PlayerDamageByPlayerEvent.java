package ru.dest.library.event;

import com.google.common.base.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageByPlayerEvent extends EntityDamageByEntityEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerDamageByPlayerEvent(@NotNull Player damager, @NotNull Player entity, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damager, entity, cause, damage);
    }

    @NotNull
    public Player getDamager(){
        return (Player) super.getDamager();
    }

    public Player getPlayer(){
        return (Player) getEntity();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList(){
        return handlers;
    }

}
