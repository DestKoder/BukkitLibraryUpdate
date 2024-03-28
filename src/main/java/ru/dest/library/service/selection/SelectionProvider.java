package ru.dest.library.service.selection;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import ru.dest.library.service.IService;

public interface SelectionProvider extends IService {

    default ISelection getSelection(Player player){throw new NotImplementedException();};;

}
