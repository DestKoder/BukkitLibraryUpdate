package ru.dest.library.service.schematic;

import org.bukkit.World;
import ru.dest.library.object.BlockPosition;

import java.io.File;

public interface SchematicService {

    void paste(World world, BlockPosition corner, File schematic);

}
