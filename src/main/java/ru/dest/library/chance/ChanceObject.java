package ru.dest.library.chance;

import lombok.Getter;

@Getter
public class ChanceObject {

    private final double chance;

    public ChanceObject(double chance) {
        this.chance = chance;
    }

}
