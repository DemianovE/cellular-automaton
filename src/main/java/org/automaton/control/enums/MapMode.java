package org.automaton.control.enums;

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
