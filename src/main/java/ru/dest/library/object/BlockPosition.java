package ru.dest.library.object;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
public class BlockPosition {

    private final int x, y, z;

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPosition(Location location){
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Location toLocation(World world){
        return new Location(world, x, y, z);
    }
}
