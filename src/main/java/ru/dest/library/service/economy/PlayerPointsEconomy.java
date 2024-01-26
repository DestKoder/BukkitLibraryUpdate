package ru.dest.library.service.economy;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.service.IService;

public class PlayerPointsEconomy implements IService, EconomyService {

    private final PlayerPointsAPI api;

    private PlayerPointsEconomy(PlayerPointsAPI api){
        this.api = api;
    }

    public static PlayerPointsEconomy init(Server server){
        if(!server.getPluginManager().isPluginEnabled("PlayerPoints")) return null;

        return new PlayerPointsEconomy(PlayerPoints.getInstance().getAPI());
    }

    @Override
    public void addMoney(@NotNull OfflinePlayer player, double amount) {
        api.give(player.getUniqueId(), (int)amount);
    }

    @Override
    public void takeMoney(@NotNull OfflinePlayer player, double amount) {
        api.take(player.getUniqueId(), (int)amount);
    }

    @Override
    public double getBalance(@NotNull OfflinePlayer player) {
        return api.look(player.getUniqueId());
    }
}
