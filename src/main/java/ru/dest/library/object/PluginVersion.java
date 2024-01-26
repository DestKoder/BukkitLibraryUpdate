package ru.dest.library.object;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
@Getter
public final class PluginVersion {

    private final int release;
    private final int build;
    private final int patch;

    public PluginVersion(int release, int build, int patch) {
        this.release = release;
        this.build = build;
        this.patch = patch;
    }

    @Contract("_ -> new")
    public static @NotNull PluginVersion fromString(@NotNull String str){
        String[] data = str.split("\\.");

        if(data.length >= 3)return new PluginVersion(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));

        if(data.length == 2) return new PluginVersion(Integer.parseInt(data[0]), Integer.parseInt(data[1]), 0);

        return new PluginVersion(Integer.parseInt(data[0]), 0,0);
    }

    public boolean higherThan(@NotNull PluginVersion v){
        if(release > v.getRelease()) return true;
        if(release == v.getRelease() && build > v.getBuild()) return true;
        return release == v.getRelease() && build == v.getBuild() && patch > v.getPatch();
    }

    public boolean lowerThan(@NotNull PluginVersion v){
        if(release < v.getRelease()) return true;
        if(release == v.getRelease() && build < v.getBuild()) return true;
        return release == v.getRelease() && build == v.getBuild() && patch < v.getPatch();
    }

    public boolean higherOrEquals(PluginVersion v){
        if(equals(v)) return true;
        return higherThan(v);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PluginVersion that = (PluginVersion) object;
        return release == that.release && build == that.build && patch == that.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(release, build, patch);
    }
}
