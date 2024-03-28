package ru.dest.library.service.schematic;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.World;
import ru.dest.library.object.BlockPosition;
import ru.dest.library.service.IService;

import java.io.File;

public interface SchematicService extends IService {

   default void paste(World world, BlockPosition corner, File schematic){throw new NotImplementedException();};;

}
