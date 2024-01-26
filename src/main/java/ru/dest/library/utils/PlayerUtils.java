package ru.dest.library.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.Library;
import ru.dest.library.gui.GUI;
import ru.dest.library.object.BukkitItem;

import java.util.List;
import java.util.UUID;

public class PlayerUtils {
    public static String getIp(@NotNull Player player){
        return player.getAddress().getAddress().getHostAddress();
    }

    public static void give(@NotNull Player player, ItemStack item){
        int slot = player.getInventory().first(item);

        if(slot != -1){
            ItemStack i = player.getInventory().getItem(slot);
            assert i != null;
            i.setAmount(i.getAmount() + item.getAmount());
            player.getInventory().setItem(slot, i);
            return;
        }
        slot = player.getInventory().firstEmpty();

        if(slot != -1) player.getInventory().addItem(item);
        else player.getWorld().dropItemNaturally(player.getLocation(), item);
    }

    public static void give(@NotNull Player player, @NotNull BukkitItem item){
        give(player, item.getItem());
    }

    public static Player getActionedOnlinePlayer(String s, boolean perUUID){
        return perUUID ? Bukkit.getPlayer(UUID.fromString(s)) : Bukkit.getPlayer(s);
    }

    public static void openGUI(GUI gui){
        Library.getInstance().g(gui);
    }

    public static boolean takeItem(@NotNull Player player, @NotNull ItemStack item, int amount){
        item.setAmount(1);
        PlayerInventory inventory = player.getInventory();

        int slot = inventory.first(item);

        if(slot == -1) return false;
        ItemStack i = inventory.getItem(slot);

        assert i != null;
        if(i.getAmount() - amount <= 0){
            return false;
        }

        if(i.getAmount() - amount == 0){
            inventory.setItem(slot, ItemUtils.EMPTY);
        }else {
            i.setAmount(i.getAmount()-amount);
            inventory.setItem(slot,i);
        }
        return true;
    }

    public static boolean takeItem(@NotNull Player player, @NotNull BukkitItem item, int amount){
        return takeItem(player, item.getItem(), amount);
    }
}
