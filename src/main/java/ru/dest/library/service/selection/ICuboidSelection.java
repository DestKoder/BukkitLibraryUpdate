package ru.dest.library.service.selection;

import org.apache.commons.lang.NotImplementedException;
import ru.dest.library.object.BlockPosition;

public interface ICuboidSelection extends ISelection{

    default BlockPosition getMinPoint(){throw new NotImplementedException();};;
    default BlockPosition getMaxPoint(){throw new NotImplementedException();};;

}
