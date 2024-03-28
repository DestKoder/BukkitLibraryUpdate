package ru.dest.library.service.region;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.World;
import ru.dest.library.object.BlockPosition;
import ru.dest.library.service.IService;

public interface RegionService<REGION extends IRegion> extends IService {

    default REGION getRegionAt(Location location) {throw new NotImplementedException();};
    default REGION getRegion(String id, World world){throw new NotImplementedException();};;

    default boolean hasRegion(String id, World world){throw new NotImplementedException();};;
    default boolean hasRegionAt(String id, Location location){throw new NotImplementedException();};;

    default REGION create(World world, BlockPosition pos1, BlockPosition pos2){throw new NotImplementedException();};;
    default REGION create(World world, String id, BlockPosition pos1, BlockPosition pos2){throw new NotImplementedException();};;

    default void remove(World world, String id){throw new NotImplementedException();};;
}
