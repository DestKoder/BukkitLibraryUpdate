package ru.dest.library.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when entity was damaged by player
 */
public class EntityDamageByPlayerEvent extends EntityDamageByEntityEvent {

    private static final HandlerList handlers = new HandlerList();

    public EntityDamageByPlayerEvent(@NotNull Player damager, @NotNull Entity damagee, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damager, damagee, cause, damage);
    }

    @NotNull
    public Player getDamager(){
        return (Player) super.getDamager();
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
