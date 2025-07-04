package org.automaton.controll.game;

import lombok.Getter;
import org.automaton.controll.common.HasDisplayName;

@Getter
public enum NeighborhoodType implements HasDisplayName {
    VON_NEUMANN("Von Neumann"),
    VON_NEUMANN_EXTENDED("Von Neumann ext."),
    MOORE("Moore");

    private final String displayName;

    NeighborhoodType(String displayName) {
        this.displayName = displayName;
    }

}
