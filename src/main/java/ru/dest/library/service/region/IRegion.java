package ru.dest.library.service.region;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import ru.dest.library.object.BlockPosition;

import java.util.List;

public interface IRegion {

    String getId();

    BlockPosition getMinimumPoint();
    BlockPosition getMaximumPoint();

    int getPriority();

    List<OfflinePlayer> getOwners();
    List<OfflinePlayer> getMembers();

    default boolean isMember(OfflinePlayer player){
        return getMembers().contains(player);
    }

    default boolean isOwner(OfflinePlayer player){
        return getOwners().contains(player);
    }

    default boolean isInRegion(Location loc){
        return isInRegion(new BlockPosition(loc));
    }
    boolean isInRegion(BlockPosition position);
}
