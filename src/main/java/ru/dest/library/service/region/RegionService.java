package ru.dest.library.service.region;

import org.bukkit.Location;
import org.bukkit.World;
import ru.dest.library.service.IService;

public interface RegionService<REGION extends IRegion> extends IService {

    REGION getRegionAt(Location location);
    REGION getRegion(String id, World world);

    boolean hasRegion(String id, World world);
    boolean hasRegionAt(String id, Location location);
}
