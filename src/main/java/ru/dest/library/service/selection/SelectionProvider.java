package ru.dest.library.service.selection;

import org.bukkit.entity.Player;

public interface SelectionProvider {

    ISelection getSelection(Player player);

}
