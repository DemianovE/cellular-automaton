package org.automaton.control.game;

import lombok.Getter;

@Getter
public enum MapMode {
    INFINITE("Infinite"),
    FINITE("Finite");

    private final String displayName;

    MapMode(String displayName) {
        this.displayName = displayName;
    }

}
