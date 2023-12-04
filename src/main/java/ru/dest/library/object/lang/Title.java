package ru.dest.library.object.lang;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.object.Pair;

import java.util.List;

public interface Title {

    @NotNull
    Title formatTitle(@NotNull List<Pair<String, String>> format);
    @NotNull
    Title formatSubTitle(@NotNull List<Pair<String, String>> format);

    @NotNull
    default Title formatAll(@NotNull List<Pair<String,String>> format){
        Title t = formatTitle(format);
        t = formatSubTitle(format);
        return t;
    }

    void send(Player player);

    @NotNull
    Object getRawTitle();
    @Nullable
    Object getRawSubtitle();
}
