package ru.dest.library.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.Library;
import ru.dest.library.bukkit.BukkitListener;
import ru.dest.library.event.*;

public class EventListener extends BukkitListener<Library> {

    public EventListener(Library plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEBPDamage(@NotNull EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)) return;

        EntityDamageByPlayerEvent e = new EntityDamageByPlayerEvent((Player) event.getDamager(), event.getEntity(), event.getCause(), event.getDamage());

        plugin.getPluginManager().callEvent(e);

        event.setDamage(e.getDamage());
        event.setCancelled(e.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPBEDamage(@NotNull EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        PlayerDamageByEntityEvent e = new PlayerDamageByEntityEvent(event.getDamager(), (Player) event.getEntity(), event.getCause(), event.getDamage());

        plugin.getPluginManager().callEvent(e);

        event.setDamage(e.getDamage());
        event.setCancelled(e.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPBPDamage(@NotNull EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        PlayerDamageByPlayerEvent e = new PlayerDamageByPlayerEvent((Player) event.getDamager(), (Player) event.getEntity(), event.getCause(), event.getDamage());

        plugin.getPluginManager().callEvent(e);

        event.setDamage(e.getDamage());
        event.setCancelled(e.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemInteract(@NotNull PlayerInteractEvent event){
        if(!event.hasItem()) return;

        ItemStack item = event.getItem();
        assert item != null;

        PlayerItemInteractEvent e = new PlayerItemInteractEvent(event.getPlayer(), event.getAction(), item, event.getClickedBlock(), event.getBlockFace(), event.getHand());

        plugin.getPluginManager().callEvent(e);

        event.setCancelled(e.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLocationChange(@NotNull PlayerMoveEvent event){
        if(event.getTo() == null) return;
        if(event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        PlayerLocationChangeEvent e = new PlayerLocationChangeEvent(event.getPlayer(), event.getFrom(), event.getTo());

        plugin.getPluginManager().callEvent(e);

        event.setCancelled(e.isCancelled());
    }
}
