package ru.dest.library.nms;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public enum NMS {
    v1_12_R1("ru.dest.library.nms.v1_12_R1.TagUtils" ),
    v1_14_R1("ru.dest.library.nms.v1_14_MORE.TagUtils"),
    v1_14_R2("ru.dest.library.nms.v1_14_MORE.TagUtils"),
    v1_16_R3("ru.dest.library.nms.v1_14_MORE.TagUtils"),
    v1_17_R1("ru.dest.library.nms.v1_14_MORE.TagUtils"),
    ;

    private final String tagUtilsClass;

    NMS(String tagUtilsClass) {
        this.tagUtilsClass = tagUtilsClass;
    }

    public @Nullable TagUtils createTagUtils() {
        try {
            return (TagUtils) Class.forName(tagUtilsClass).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            return null;
        }
    }
}
