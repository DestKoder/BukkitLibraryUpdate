package ru.dest.library.chance;

import java.util.ArrayList;

public class ChanceObjectContainer<T extends ChanceObject> extends ArrayList<T> {

    public T getRandomItem() {
        double random = Math.random();
        T result = null;

        for(T t  : this) {
            if(random <= t.getChance()) {
                if(result != null && t.getChance() > result.getChance() ) continue;
                result = t;
            }
        }
        if(result == null) return getRandomItem();
        else return result;
    }
    
}
