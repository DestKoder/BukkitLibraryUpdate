package ru.dest.library.cooldown;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a information of cooldown
 *
 * @since 1.0
 * @author DestKoder
 */
@Getter
public class CooldownData {

    private final String action;
    @Setter
    private long expires;

    public CooldownData(String action, long expires) {
        this.action = action;
        this.expires = expires;
    }
}
