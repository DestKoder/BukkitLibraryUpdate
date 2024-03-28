package ru.dest.library.service.selection;

import org.apache.commons.lang.NotImplementedException;
import ru.dest.library.object.BlockPosition;

import java.util.List;

public interface ISelection {

    default List<BlockPosition> getPositions(){throw new NotImplementedException();};;

}
