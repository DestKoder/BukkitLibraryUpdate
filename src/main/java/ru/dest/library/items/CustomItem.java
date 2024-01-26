package ru.dest.library.items;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.Library;
import ru.dest.library.event.EntityDamageByPlayerEvent;
import ru.dest.library.event.PlayerItemInteractEvent;
import ru.dest.library.object.BukkitItem;
import ru.dest.library.object.RegistryKey;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.PlayerUtils;
import ru.dest.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomItem implements BukkitItem {

    @Getter
    private final RegistryKey key;
    private final ItemStack bukkitItem;

    private final String name;
    private final List<String> lore;

    public CustomItem(@NotNull RegistryKey key, @NotNull ItemStack bukkitItem) {
        this.key = key;

        ItemMeta meta = bukkitItem.getItemMeta();
        assert meta != null;
        if(meta.hasDisplayName()) this.name = ColorUtils.parse(meta.getDisplayName());
        else this.name = key.getId();
        if(meta.getLore() != null) this.lore = ColorUtils.parse(meta.getLore());
        else this.lore = new ArrayList<>();

        this.bukkitItem =  Library.getInstance().getNbtUtils().setStringTag(bukkitItem, Library.getInstance().getItemId(), this.key.toString());

        ItemRegistry.get().register(this);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if(o.getClass() == o.getClass()){
            CustomItem that = (CustomItem) o;

            if (!Objects.equals(key, that.key)) return false;
            return Objects.equals(bukkitItem, that.bukkitItem);
        }else if (o.getClass() == ItemStack.class){
            ItemStack i = (ItemStack) o;
            CustomItem item = ItemRegistry.get().getItem(i);
            return equals(item);
        }else return false;
    }

    @Deprecated
    public CustomItem(@NotNull NamespacedKey key, @NotNull ItemStack bukkitItem) {
        this.key = RegistryKey.fromString(key.toString());

        ItemMeta meta = bukkitItem.getItemMeta();
        assert meta != null;
        if(meta.hasDisplayName()) this.name = ColorUtils.parse(meta.getDisplayName());
        else this.name = this.key.getId();
        if(meta.getLore() != null) this.lore = ColorUtils.parse(meta.getLore());
        else this.lore = new ArrayList<>();

        this.bukkitItem =  Library.getInstance().getNbtUtils().setStringTag(bukkitItem, Library.getInstance().getItemId(), this.key.toString());;

        ItemRegistry.get().register(this);
    }

    @Override
    public final ItemStack getItem() {
        return bukkitItem;
    }

    protected void onGive(ItemStack item){}

    public final void give(Player player, int amount){
        ItemStack toGive = this.bukkitItem.clone();
        toGive.setAmount(amount);

        ItemMeta meta = toGive.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ColorUtils.parse(Utils.applyPlaceholders(name, player)));
        List<String > lore = new ArrayList<>();

        for (String s : this.lore) {
            lore.add(ColorUtils.parse(Utils.applyPlaceholders(s, player)));
        }

        meta.setLore(lore);

        toGive.setItemMeta(meta);

        this.onGive(toGive);

        PlayerUtils.give(player, toGive);
    }

    public void onDrop(PlayerDropItemEvent event){}

    public void onInteract(PlayerItemInteractEvent event){}

    public void onConsume(PlayerItemConsumeEvent event){}

    public void onDamage(EntityDamageByEntityEvent event){}

    public void onProtect(EntityDamageByEntityEvent event){}

    public void onBlockPlace(BlockPlaceEvent event){}

    public void onBlockBreak(BlockBreakEvent event){}
}
