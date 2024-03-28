package ru.dest.library.service.region;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import ru.dest.library.object.BlockPosition;

import java.util.List;

public interface IRegion {

    default String getId(){throw new NotImplementedException();};;

    default BlockPosition getMinimumPoint(){throw new NotImplementedException();};;
    default BlockPosition getMaximumPoint(){throw new NotImplementedException();};;

    default int getPriority(){throw new NotImplementedException();};;

    default List<OfflinePlayer> getOwners(){throw new NotImplementedException();};;
    default List<OfflinePlayer> getMembers(){throw new NotImplementedException();};;

    default boolean isMember(OfflinePlayer player){
        return getMembers().contains(player);
    }

    default boolean isOwner(OfflinePlayer player){
        return getOwners().contains(player);
    }

    default boolean isInRegion(Location loc){
        return isInRegion(new BlockPosition(loc));
    }
    default boolean isInRegion(BlockPosition position){throw new NotImplementedException();};;

    default void setFlag(String name, Object o){throw new NotImplementedException();};;
}
