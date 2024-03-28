package ru.dest.library.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public static boolean hasItem(@NotNull Player player, ItemStack item, int amount){
        PlayerInventory inventory = player.getInventory();

        for(int i = 0; i < inventory.getSize(); i++){
            ItemStack it = inventory.getItem(i);

            if(it != null && it.getType() == item.getType()){
                amount-= it.getAmount();
            }
        }
        return amount <= 0;
    }

    public static boolean takeItem(@NotNull Player player, @NotNull ItemStack item, int amount){
        PlayerInventory inventory = player.getInventory();

        while (amount > 0){
            boolean found = false;
            for(int i = 0; i < inventory.getSize(); i++){
                ItemStack itm = inventory.getItem(i);
                if(itm != null && itm.getType().equals(item.getType())){
                    found = true;
                    int amt = itm.getAmount() - 1;
                    amount-=1;
                    itm.setAmount(amt);
                    inventory.setItem(i, amt > 0 ? itm : ItemUtils.EMPTY);
                    player.updateInventory();
                    break;
                }
            }
            if(!found){
                return false;
            }
        }

        return true;
    }

    public static void sendTitle(@NotNull Player player, String title, @Nullable String subtitle){
        player.sendTitle(ColorUtils.parse(Utils.applyPlaceholders(title, player)), subtitle == null ? "" : ColorUtils.parse(Utils.applyPlaceholders(subtitle, player)), 20,20,20);
    }

    public static boolean takeItem(@NotNull Player player, @NotNull BukkitItem item, int amount){
        return takeItem(player, item.getItem(), amount);
    }
}
