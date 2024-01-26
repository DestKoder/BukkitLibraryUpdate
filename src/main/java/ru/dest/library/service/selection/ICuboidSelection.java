package ru.dest.library.service.selection;

import ru.dest.library.object.BlockPosition;

public interface ICuboidSelection extends ISelection{

    BlockPosition getMinPoint();
    BlockPosition getMaxPoint();

}
