package ru.dest.library.impl.locale;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dest.library.object.Pair;
import ru.dest.library.object.lang.Title;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.StringUtils;

import java.util.List;

public final class TitleImpl implements Title {

    private String title;
    private String subtitle;

    public TitleImpl(@NotNull String title,@Nullable String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public @NotNull Title formatTitle(@NotNull List<Pair<String, String>> format) {
        title = StringUtils.format(title, format);
        return this;
    }

    @Override
    public @NotNull Title formatSubTitle(@NotNull List<Pair<String, String>> format) {
        subtitle = StringUtils.format(subtitle, format);
        return this;
    }

    @Override
    public void send(@NotNull Player player) {
        player.sendTitle(ColorUtils.parse(title), ColorUtils.parse( subtitle == null ? "" : subtitle), 20, 20 ,20);
    }

    @Override
    public @NotNull Object getRawTitle() {
        return title;
    }

    @Override
    public @Nullable Object getRawSubtitle() {
        return subtitle;
    }
}
