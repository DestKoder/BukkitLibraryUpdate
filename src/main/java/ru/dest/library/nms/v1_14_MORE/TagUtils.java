package ru.dest.library.nms.v1_14_MORE;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TagUtils implements ru.dest.library.nms.TagUtils {

    public TagUtils(){};

    @Override
    public ItemStack setStringTag(@NotNull ItemStack item, NamespacedKey key, String value) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack setIntegerTag(@NotNull ItemStack item, NamespacedKey key, int value) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack setDoubleTag(@NotNull ItemStack item, NamespacedKey key, double value) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return item;

        meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getStringTagValue(@NotNull ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return null;

        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        //return item;
    }

    @Override
    public Integer getIntegerTagValue(@NotNull ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return null;

        return meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
    }

    @Override
    public Double getDoubleTagValue(@NotNull ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return null;

        return meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
    }

    @Override
    public boolean hasTag(@NotNull ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;

        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

}
