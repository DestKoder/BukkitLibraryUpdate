package ru.dest.library.command;

import java.util.List;

public interface ICommand {

    String getName();
    List<String> getAliases();
}
