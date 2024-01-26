package ru.dest.library.impl.locale;

import org.jetbrains.annotations.NotNull;
import ru.dest.library.locale.Lang;
import ru.dest.library.object.lang.Message;
import ru.dest.library.object.lang.Title;
import ru.dest.library.utils.ColorUtils;

import java.util.Map;

public class LangImpl implements Lang {

    private final Map<String, String> data;

    public LangImpl(Map<String, String> data) {
        this.data = data;
    }


    @Override
    public @NotNull String getValue(@NotNull String key) {
        return ColorUtils.parse(data.getOrDefault(key, key));
    }

    @Override
    public @NotNull Message getMessage(@NotNull String key) {
        return new MessageImpl(getValue(key));
    }

    @Override
    public @NotNull Title getTitle(@NotNull String key) {
        String title = getValue(key + ".title");
        String subKey = key + ".subtitle";
        String subtitle = (data.containsKey(subKey) ? getValue(subKey) : null);

        return new TitleImpl(title, subtitle);
    }
}
