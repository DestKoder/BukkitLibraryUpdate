package ru.dest.library.core.listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import ru.dest.library.Library;
import ru.dest.library.bukkit.BukkitListener;
import ru.dest.library.event.PlayerItemInteractEvent;
import ru.dest.library.items.CustomItem;
import ru.dest.library.items.ItemRegistry;

public class ItemListener extends BukkitListener<Library> {

    public ItemListener(Library plugin) {
        super(plugin);
    }

    @EventHandler
    public void handle(PlayerItemInteractEvent event){
        ItemStack item = event.getItem();
        CustomItem ci = ItemRegistry.get().getItem(item);

        if(ci == null) return;

        ci.onInteract(event);
    }

    @EventHandler
    public void handle(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        CustomItem ci = ItemRegistry.get().getItem(item);

        if(ci == null) return;

        ci.onDrop(event);
    }

    @EventHandler
    public void handle(PlayerItemConsumeEvent event){
        ItemStack item = event.getItem();
        CustomItem ci = ItemRegistry.get().getItem(item);

        if(ci == null) return;

        ci.onConsume(event);
    }

    @EventHandler
    public void handle(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof LivingEntity)) return;
        if(!(event.getDamager() instanceof LivingEntity)) return;

        LivingEntity e = (LivingEntity) event.getEntity();
        LivingEntity damager = (LivingEntity) event.getDamager();

        EntityEquipment equipment = e.getEquipment();
        if(equipment != null && equipment.getArmorContents() != null) for(ItemStack item : equipment.getArmorContents()){
            CustomItem i = ItemRegistry.get().getItem(item);
            if(i != null) i.onProtect(event);
        }

        if(damager.getEquipment() != null && damager.getEquipment().getItemInMainHand().getType() != Material.AIR){
            CustomItem i = ItemRegistry.get().getItem(damager.getEquipment().getItemInMainHand());
            if( i != null) i.onDamage(event);
        }

    }

}
