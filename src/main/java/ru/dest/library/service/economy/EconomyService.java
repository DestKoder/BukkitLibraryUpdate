package ru.dest.library.service.economy;

import org.bukkit.OfflinePlayer;

public interface EconomyService {

    void addMoney(OfflinePlayer player, double amount);

    void takeMoney(OfflinePlayer player, double amount);

    double getBalance(OfflinePlayer player);
}
