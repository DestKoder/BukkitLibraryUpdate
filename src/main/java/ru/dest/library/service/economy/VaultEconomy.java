package ru.dest.library.service.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.service.IService;

public class VaultEconomy implements IService, EconomyService {

    private final Economy economy;

    private VaultEconomy(Economy economy){
        this.economy = economy;
    }

    public static @Nullable VaultEconomy init(@NotNull Server server){
        if(!server.getPluginManager().isPluginEnabled("Vault")){
            return null;
        }

        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);

        if(rsp == null) return null;

        return new VaultEconomy(rsp.getProvider());
    }

    @Override
    public void addMoney(OfflinePlayer player, double amount) {
        economy.depositPlayer(player, amount);
    }

    @Override
    public void takeMoney(OfflinePlayer player, double amount) {
        economy.withdrawPlayer(player, amount);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }
}
